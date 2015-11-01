package drum;


import java.io.*;
import java.net.*;

public class Client{
  
  private int serverPort;
  private InetAddress serverIp ;
  private Socket sock ;
  private DataOutputStream os;
  private FileInputStream fis ;
  
  public Client(String serverName,int port){
    try{
    this.serverIp = InetAddress.getByName(serverName) ;
    this.serverPort = port;
    }catch(UnknownHostException e){
      System.err.println("Find server ip error : "+e.toString());
    }
  }
  
  public void sentFile (File file) {
    int i;
    try{
      //serverIp = InetAddress.getByName("localhost") ;
      fis = new FileInputStream (file);
      
      
      while ((i = fis.read()) > -1){
        os.write(i);
      }
      closeSocket();
      System.out.println("completed");
    }catch(Exception e){
      System.out.println(e);
    }
  }
  
  private void openSocket(){
    try{
    sock = new Socket(serverIp, serverPort);
    os = new DataOutputStream(sock.getOutputStream());
    }catch(IOException e){
      System.err.println("Socket open error : "+e.toString());
    }
  }
  
  private void closeSocket(){
    try{
      fis.close();
      os.close();
      sock.close();
    } catch(IOException e){
      System.err.println("Socket close error : "+e.toString());
    }
  }
}
