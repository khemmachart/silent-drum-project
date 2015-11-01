/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drum;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author athitiya
 */
public class AcceptConnection implements Runnable, ServerAcceptInterface{
    
    public static String x = "eiei gum";
    
    private ServerSocket socket;
    Thread thread;
    private ArrayList<Socket> clientList;
    public AcceptConnection(ServerSocket socket,ArrayList<Socket> list) {
        this.socket = socket;
        this.thread = new Thread(this);
        this.clientList = list;
        this.thread.start();
        System.err.println("Create AcceptConnection");
    }

    @Override
    public void run() {
        System.err.println("Run AcceptConnection1");
        acceptConnect();
        System.err.println("Run AcceptConnection2");
    }

    @Override
    public void acceptConnect() {
        try {
            while(true){
                Socket tmp = socket.accept();
                clientList.add(tmp);
                for(Socket sock : clientList){
                    x = sock.getLocalAddress().getHostAddress();
                    System.err.println(x);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AcceptConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
