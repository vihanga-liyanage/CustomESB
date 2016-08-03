/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package middleware.esb;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vihanga Liyanage
 */
public class ESBServer {
    static ServerSocket ss;
    static ClientThread[] clients = new ClientThread[10];
    static int index = 0;
    static DataOutputStream dout;
    
    static Dictionary serviceList = new Hashtable();

    public static void main(String[] args) {
        String msgIn = "";
        
        try {    
            ss = new ServerSocket(1201);
            System.out.println("Server started.\nWaiting for clients...");
            //new MessageServer().setVisible(true);
            while(index < 10){
                Socket s = ss.accept();
                clients[index] = new ClientThread(s);
                clients[index].start();
                System.out.println("Client " + index + " " + s.getInetAddress().getHostName() + " connected.");
                //sending acknowledgement
                dout = new DataOutputStream(clients[index].s.getOutputStream());
                dout.writeUTF(index + "");
                index++;
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void clientRequest(String msg){
        System.out.println(msg);
        //Extracting client id
        int clientId = Integer.parseInt(msg.substring(0, 1));
        
        try {
            dout = new DataOutputStream(clients[clientId].s.getOutputStream());
            
            //If it's a service that requests the server...
            if(msg.contains("AddService")){
                //msg format : <id>AddService<service name>
                String serviceName = msg.substring(11, msg.length());
                serviceList.put(serviceName, clientId);
                dout.writeUTF("AddService-RequestAccepted-" + serviceName);
                printServiceList();
            } else if (msg.contains("ServiceReply")){
                String params[] = msg.split(",");
                int newClientId = Integer.parseInt(params[2]);
                String ans = params[3];
                dout = new DataOutputStream(clients[newClientId].s.getOutputStream());
                dout.writeUTF(ans);
            } else {
                String params[] = msg.split(",");
                String serviceName = params[1];
                String param1 = params[2];
                String param2 = params[3];
                int serviceId = -1;
                
                //searching for the service
                Enumeration e = serviceList.keys();
                while (e.hasMoreElements()){
                    String key = (String) e.nextElement();
                    if(key.equals(serviceName)){
                        serviceId = (int)serviceList.get(key);
                        break;
                    }
                }
                
                if(-1 != serviceId){
                    dout = new DataOutputStream(clients[serviceId].s.getOutputStream());
                    dout.writeUTF(clientId + "," + param1 + "," + param2);
                    
                } else {
                    //error handling
                    dout.writeUTF("Error : Service not found");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ESBServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void printServiceList(){
        Enumeration e = serviceList.keys();
        while (e.hasMoreElements()){
            String key = (String) e.nextElement();
            System.out.println(key + ":" + serviceList.get(key));
        }
        
    }
    
}
