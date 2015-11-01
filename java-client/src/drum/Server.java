package drum;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

public class Server{
    private ServerSocket socket;
    
    private int port = 23456;
    private ArrayList<Socket> client = new ArrayList<Socket>();
    private AcceptConnection ac;
    
public Server(){
    
        try {
            System.out.println("Start Server");
            socket  = new ServerSocket(port);
            socket.setReuseAddress(true);
            ac = new AcceptConnection(socket, client);
           // ss = new SentSound(client);
            System.out.println("server construct");
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("server construct : "+ex.toString());
        }
        
       
    }

   public void sentSound(File sound) throws IOException {
        System.out.println(sound);
        
        
        try{
      
        for(int i = 0; i < client.size();i++){
            System.out.println(client.get(i).getLocalAddress().getHostAddress());
            /*
            DataOutputStream dis = new DataOutputStream(client.get(i).getOutputStream());
            FileInputStream fout = new FileInputStream(sound);
            int ij;
            System.out.println("1");
            while ( (ij = fout.read()) > -1) {
              dis.write(ij);
                System.out.println("Success");
            }

            //fout.flush();
            fout.close();
            dis.close();
                    */
            }
        
        
      
    }catch(Exception e){
      System.err.println(e.getMessage());
    }finally{
            socket.close();
        }
       
  
        
    }
  
}