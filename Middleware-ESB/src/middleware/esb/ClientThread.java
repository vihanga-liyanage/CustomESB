/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package middleware.esb;

import java.io.*;
import java.net.Socket;

/**
 *
 * @author Vihanga Liyanage
 */
public class ClientThread extends Thread{
    public Socket s;

    public ClientThread(Socket s) {
        this.s = s;
        System.out.println("thread started.");
    }
    @Override
    public void run(){
        DataInputStream din;
        try {
            din = new DataInputStream(s.getInputStream());
            String name = s.getInetAddress().getHostName();
            
            String msgIn = "";
            while(!msgIn.equals("exit")){
                msgIn = din.readUTF();
                ESBServer.clientRequest(msgIn);
                //MessageServer.msgArea.setText(MessageServer.msgArea.getText() + "\n" + name + " : " + msgIn);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
