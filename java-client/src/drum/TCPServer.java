/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drum;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
 
/**
 * The class extends the Thread class so we can receive and send messages at the same time
 */
public class TCPServer extends Thread {
 
//    public static final int SERVERPORT = 23457;
    public static final int SERVERPORT = 3000;
    private boolean running = false;
    private PrintWriter mOut;
    private OnMessageReceived messageListener;
 
    public static void main(String[] args) throws IOException {
 
        //opens the window where the messages will be received and sent
        NewFrame frame = new NewFrame();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
 
    }
 
    /**
     * Constructor of the class
     * @param messageListener listens for the messages
     */
    public TCPServer(OnMessageReceived messageListener) {
        this.messageListener = messageListener;
        System.out.print("hello1");
    }

    TCPServer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    /**
     * Method to send the messages from server to client
     * @param message the message sent by the server
     */
    public void sendMessage(String message){
        if (mOut != null && !mOut.checkError()) {
            mOut.println(message);
            mOut.flush();
            System.out.print("hello2");
        }
    }
 
    @Override
    public void run() {
        super.run();
 
        running = true;
 
        try {
            System.out.println("S: Connecting...");
            System.out.print("hello3");
 
            //create a server socket. A server socket waits for requests to come in over the network.
            ServerSocket serverSocket = new ServerSocket(SERVERPORT);
 
            //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
            Socket client = serverSocket.accept();
            System.out.println("S: Receiving...");
            System.out.print("hello5");
 
            try {
 
                //sends the message to the client
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                System.out.print("hello6");
 
                //read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
 
                //in this while we wait to receive messages from client (it's an infinite loop)
                //this while it's like a listener for messages
                while (running) {
                    String message = in.readLine();
                    System.out.print("hello7");
                    if (message != null && messageListener != null) {
                        //call the method messageReceived from ServerBoard class
                        messageListener.messageReceived(message);
                    }
                }
 
            } catch (Exception e) {
                System.out.println("S: Error");
                System.out.print("hello8");
                e.printStackTrace();
            } finally {
                client.close();
                System.out.println("S: Done.");
                System.out.print("hello9");
            }
 
        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
            System.out.print("hello10");
        }
 
    }
 
    //Declare the interface. The method messageReceived(String message) will must be implemented in the ServerBoard
    //class at on startServer button click
    public interface OnMessageReceived {
        
        public void messageReceived(String message);
       
    }
 
}