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

/**
 *
 * @author Vihanga Liyanage
 */
public class ESBClient {
    static Socket socket;
    static DataInputStream din;
    static DataOutputStream dout;
    static int id;
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new MessageClient().setVisible(true);
            }
        });
        
        String msgIn = "";
        try {    
            socket = new Socket("127.0.0.1", 1201);
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            String name = socket.getInetAddress().getHostName();
            System.out.println("Connected to server " + name);
            //
            msgIn = din.readUTF();
            id = Integer.parseInt(msgIn);
            System.out.println("My ID " + id);
            System.out.print("Type request : ");
            Scanner scanner = new Scanner(System.in);
            String msgOut = scanner.next();
            dout.writeUTF(id + msgOut);
            //
            while(!msgIn.equals("exit")){
                msgIn = din.readUTF();
                System.out.print("Reply from server : ");
                System.out.println(msgIn);
                //msgArea.append("\n" + name +" : " + msgIn);
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
