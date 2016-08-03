/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package middleware.esb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import static middleware.esb.ESBClient.din;
import static middleware.esb.ESBClient.socket;

/**
 *
 * @author Vihanga Liyanage
 */
public class ESBServiceAdd{
    static Socket socket;
    static DataInputStream din;
    static DataOutputStream dout;
    static int id;
    
    public static int function(int i, int j) {
        return i + j;
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
        
        String msgIn = "";
        try {    
            //creating socket and establishing connection
            socket = new Socket("127.0.0.1", 1201);
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            String name = socket.getInetAddress().getHostName();
            System.out.println("Connected to server " + name);
            //get the client id from server
            msgIn = din.readUTF();
            id = Integer.parseInt(msgIn);
            System.out.println("My ID " + id);
            
            //Requesting server to add the service to it's provider list
            dout.writeUTF(id + "AddService" + "Add");
            //get server confirmation
            msgIn = din.readUTF();
            if(msgIn.equals("AddService-RequestAccepted-" + "Add")){
                System.out.println("Service added succefully. Awaiting for client requests...");
                //waiting for requests from servers
                while(!msgIn.equals("exit")){
                    msgIn = din.readUTF();
                    System.out.println("Request from server : " + msgIn);
                    //request format : clientID,param1,param2
                    String params[] = msgIn.split(",");
                    String clientID = params[0];
                    int n1 = Integer.parseInt(params[1]);
                    int n2 = Integer.parseInt(params[2]);
                    int ans = function(n1, n2);
                    System.out.println("Reply for server : " + ans);
                    dout.writeUTF(id + ",ServiceReply," + clientID + "," + ans);
                }
            } else {
                System.out.println("Error occured while adding the service!");
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
