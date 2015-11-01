/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author athitiya
 */
public class SentSound implements SentSoundInterface, Runnable{
    private ArrayList<Socket> clientList;
    
    Thread thread;
    
    
    
    public SentSound(ArrayList<Socket> list) {
    this.clientList = list;
    
    this.thread = new Thread(this);
    }

    public SentSound() {
        
    }
    

    
    
    
    

    @Override
    public void sentSound(File sound) {
      //public void sentSound(int sound){
        System.out.println(sound);
        
        
        try{
      
        for(int i = 0; i < clientList.size();i++){
            System.out.println(clientList);
            DataOutputStream dis = new DataOutputStream(clientList.get(i).getOutputStream());
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
            }
        
        
      
    }catch(IOException e){
      System.err.println(e.getMessage());
    }/*catch(FileNotFoundException e){
      System.err.println(e.toString());
    }*/catch(Exception e){
      System.err.println(e.getMessage());
    }
  
        
    }

    @Override
    public void run() {
       
        
    }

    
    
}
