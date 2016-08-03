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
        int clientId = Integer.parseInt(msg.substring(0, 1));
        try {
            dout = new DataOutputStream(clients[clientId].s.getOutputStream());
            dout.writeUTF("Hello client " + clientId);
        } catch (IOException ex) {
            Logger.getLogger(ESBServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
