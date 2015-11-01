package drum;

import drum.Matching;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import java.util.ArrayList;
import java.*;
import java.net.Socket;

public class MidiInputReceiver implements Receiver {
    
    private ArrayList<Byte> soundSignal;
    private int sum = 0;
    
    NewFrame sender;
    
    public MidiInputReceiver(NewFrame sender) {
        this.sender = sender;
        initSoundSignal();
    }

   @Override
    public void send(MidiMessage msg, long timeStamp) {
        try {
            soundSignal.add(msg.getMessage()[1]);
            if(soundSignal.size()==12){
                
                // Starting of formular
                sum = soundSignal.get(0)*
                        soundSignal.get(1)+
                                soundSignal.get(2)-
                                        soundSignal.get(3)*
                                                soundSignal.get(4)+
                                                        soundSignal.get(5)-
                                                                soundSignal.get(6)*
                                                                        soundSignal.get(7)+
                                                                                soundSignal.get(8)-
                                                                                        soundSignal.get(9)*
                                                                                                soundSignal.get(10)+
                                                                                                        soundSignal.get(11);
                sum = Math.abs(sum);
                // Ending of formular

                clearSoundSignal();
                
                String drumPosition = new Matching().getPosition(sum);
                String soundName = new Matching().playSound(drumPosition);

                sender.sendSoundString(soundName);
            }
        }
        catch (Exception e) {
                
        }
    }
    
    public void clearSoundSignal () {
        initSoundSignal();
    }
    
    public void initSoundSignal () {
        soundSignal = new ArrayList<>(); 
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
   