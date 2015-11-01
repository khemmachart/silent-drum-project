/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import sun.audio.*;

/**
 *
 * @author athitiya
 */
public class Matching {
    
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    
    public static final String ANSI_RESET = "\u001B[0m";
    
     public File tom1 = new File("TOM1.WAV");
     public File tom2 = new File("TOM2.WAV");
   
    
    public String getPosition(int sum){
        if(sum == 7408 || sum == 3986 || sum == 7095 || sum == 6284 || sum == 6231 || sum == 6147 || sum == 7036||sum == 7150 || sum == 7998 ||sum == 6238||sum== 3845||sum == 3702|| sum == 6912 || sum ==6990||sum == 7152|| sum == 6975|| sum == 8268 || sum == 3261 || sum == 6767 || sum == 4262  ){
            System.out.println(ANSI_BLACK+"Left top "+sum+ANSI_RESET);
            return "LT";
        } else if(sum == 4474 || sum == 7654 || sum==4090||sum == 6106 || sum == 7157 || sum == 5474 || sum == 6125 || sum == 4611 ||sum ==6842|| sum == 4335||sum == 7064 || sum == 5325||sum == 4194|| sum == 5255 |sum == 7786|| sum == 5403|| sum == 7924|| sum == 3610||sum==5543|| sum == 4051|| sum==4289||sum == 6871||sum ==5330 || sum==3313 || sum == 5018){
            System.out.println(ANSI_BLUE+"Center top "+sum+ANSI_RESET);
            return "CT";
        } else if(sum == 7904  || sum == 5324 ||sum ==7772|| sum == 3544 || sum == 5254||sum == 5540|| sum == 6052|| sum == 6072 ||sum == 4668 || sum == 5333 ||sum == 4527 || sum == 6238 || sum == 5398|| sum==4944||sum == 6966 ||sum ==7353||sum == 5849 || sum == 3233 || sum == 5248|| sum == 4185 || sum == 3943 || sum ==2553 || sum == 5008 || sum ==8054|| sum == 4384 ||sum ==8042 || sum ==7182 || sum == 3928 || sum ==8130 || sum ==5516 || sum == 6946){
            System.out.println(ANSI_CYAN+"Right top "+sum+ANSI_RESET);
            return "RT";
        } else if(sum == 6928 || sum == 3489 || sum ==6709 || sum == 8300 || sum == 7740 || sum == 6383 || sum ==3910|| sum == 2400 || sum == 5629|| sum == 3209|| sum == 6904 || sum == 6392 || sum==4422|| sum==6965|| sum == 7872 || sum == 7186 || sum == 6960||sum == 4005 || sum == 3066 || sum==3806||sum == 7824 || sum == 7024|| sum == 7136 || sum == 3350 ||sum==8010|| sum == 3626 || sum == 6587 || sum== 2625){
            System.out.println(ANSI_GREEN+"Left down "+sum+ANSI_RESET);
            return "LD";
        } else if(sum == 5490 || sum == 7166 || sum == 7459 || sum == 5418 || sum == 3404 || sum == 8511 || sum == 3685 || sum == 4125 || sum == 2960 || sum == 5190 || sum == 7729 || sum == 3905 || sum == 4104 || sum == 6686 || sum == 5694 || sum == 7447 || sum == 3824 || sum == 6647|| sum == 6343|| sum==6371||sum == 5628||sum == 3725||sum == 3961|| sum == 3401 || sum == 5560|| sum== 7081){
            System.out.println(ANSI_PURPLE+"Center down "+sum+ANSI_RESET);
            return "CD";
        } else if(sum == 8258 || sum == 3648 || sum == 2976 || sum == 7510 ||sum == 5359 || sum == 7130 || sum == 2112 || sum == 2696 ||sum == 3833 ||sum == 6489 || sum == 8124||sum == 7221||sum == 8322||sum == 7832 ||sum==6409||sum==3549||sum == 7491|| sum == 5709 ||sum==3113||sum == 6510 ||sum == 3712|| sum == 9091 || sum == 6631 || sum == 6521 || sum == 3729 || sum == 6711 || sum == 8192 || sum == 2837){
            System.out.println(ANSI_RED+"Right down "+sum+ANSI_RESET);
            
            return "RD";
        } else{
            System.out.println(ANSI_YELLOW+sum+ANSI_RESET);
            return "other";
        }
    }
    
    public String playSound(String position) {
       String soundName = NewFrame.setDrum.get(position).toString();
       
       try {
           AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("/Users/athitiya/Desktop/drum/" +soundName+ ".wav").getAbsoluteFile());
           Clip clip = AudioSystem.getClip();
           clip.open(audioInputStream);
           clip.start();
       } catch(Exception ex) {
           System.out.println("Error with playing sound.");
           ex.printStackTrace();
       }

       return soundName;
    }
}


