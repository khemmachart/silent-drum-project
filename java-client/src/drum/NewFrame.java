/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drum;


import javax.sound.midi.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author athitiya
 */
public class NewFrame extends java.awt.Frame{
      
    // Variable for buttons
    private ArrayList<javax.swing.JButton> drumSoundButtonList = new ArrayList<javax.swing.JButton>();
    private javax.swing.JTextArea connectStatus = new javax.swing.JTextArea();
    private javax.swing.JList statusList1 = new javax.swing.JList();
    private javax.swing.JLabel ipStatus = new javax.swing.JLabel();
    javax.swing.JTextArea status = new javax.swing.JTextArea();
    static String setStatus;
    
    //private ArrayList<javax.swing.JLabel> DisconnecttatusList = new ArrayList<javax.swing.JLabel>();
    public static Dictionary setDrum = new Hashtable ();
    private int selectedDrumSoundButtonIndex;
    
    // Drum position
    private ArrayList<String> drumPositionName;
    
    // Set server and port
    private static final int serverPort  = 80;
    private static final String serverIP = "172.20.10.2";
    
    Socket socket;
    OutputStream out;
    
    /**
     * Creates new form NewFrame
     */
    public NewFrame() throws IOException {
        
        drumPositionName = new ArrayList(Arrays.asList("Left Top", "Left Bottom", "Middle Top", "Middle Bottom", "Right Top", "Right Bottom"));
        initComponents();
        
        // Add connect status to panel1
        jPanel1.add(connectStatus);
        jPanel1.add(status);
        System.out.println();
        remove.setEnabled(false);
        disconnect.setEnabled(false);
        
        //addStatus();
    }
    
    private NewFrame(Server server) throws IOException {
        
        this();
        
        MidiDevice device;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        System.out.println(infos.length);
        
        for (MidiDevice.Info info :infos){
            try{
                device = MidiSystem.getMidiDevice(info);
                System.out.println("Info: "+info);
                List<Transmitter> transmitters = device.getTransmitters();
                System.out.println("Tranmitter = "+transmitters.size());
                for(int j = 0;j<transmitters.size();j++){
                    transmitters.get(j).setReceiver(new MidiInputReceiver(this));
                    System.out.println("new Rece");
                }
                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(this));
                device.open();
                System.out.println(device.getDeviceInfo()+" Was Opened");
            }
            catch (MidiUnavailableException e){
                System.out.println("Error"+e);
            }
        } //To change body of generated methods, choose Tools | Templates.
    }

    // This method will be called after user click set button
    // It will add the button to the list and re-paint the panel
    // After added, setEnabel of set button from checking the number of buttons
    
    private boolean initSocket () throws IOException {
        socket = new Socket(serverIP, serverPort);
        
        if (socket != null) {
            out = socket.getOutputStream();
            joinServerWithName("Drummer");
        }
        
        return socket != null;
    }
    
    private void closeSocket () throws IOException {
        out.close();
        socket.close();
    }
    
    public void joinServerWithName (String name) throws IOException {
        String joinedName = "iam:" + name;
        out.write(joinedName.getBytes());
        out.flush();
    }
    
    public void sendMessage (String message) throws IOException {
        String msg = "msg:" + message;
        out.write(msg.getBytes());
        out.flush();
    }
    
    public void sendSoundString (String message) throws IOException {
        String msg = "playSound:" + message;
        out.write(msg.getBytes());
        out.flush();
    }
    
    public void setStatus(){
        
        status.setText(setStatus);
        jPanel1.revalidate();
    }
        
    
    public void setDrumSoundButton () {
        
        addNewDrumSoundButtonToList();
        rearrangeDrumSoundButton();
        shouldDisableSetButton();
       
    }
    
    public void addConnectStatus(){
        String newText = connectStatus.getText() + "Started ..\n";
        connectStatus.setText(newText);
        jPanel1.revalidate();
        
    }
    
    public void addDisconnectStatus(){
        String newText = connectStatus.getText() + "Stoped ..\n";
        connectStatus.setText(newText);
        jPanel1.revalidate();
    }
    
    
    
/*    public void addStatus(){
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        
        statusList1.setModel(new javax.swing.AbstractListModel() {
            
            String[] strings = {};
            
            
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(statusList1);
        jPanel1.add(jScrollPane1);
        jPanel1.revalidate();
        
    }*/
    
    // Create new drumSoundButton and add it to the list
    public void addNewDrumSoundButtonToList () {
        javax.swing.JButton newButton = drumSoundButtonMake();
        drumSoundButtonList.add( newButton );
    }
    
    
        
    
    public void removeDrumPositionNameFromList (String positionName) {
        drumPositionName.remove(positionName);
        System.out.println(drumPositionName);
        
        position.setModel(new javax.swing.DefaultComboBoxModel(drumPositionName.toArray(new String[drumPositionName.size()])));
        position.revalidate();
        position.repaint();
    }
    
    
    public javax.swing.JButton drumSoundButtonMake () {
        String buttonTitle2 = sound.getSelectedItem().toString();
        String buttonTitle1 = position.getSelectedItem().toString();
        
        String drumPosition;
        String drumSound;
        
        drumSound = sound.getSelectedItem().toString();
        drumPosition = position.getSelectedItem().toString();
        
        if(drumPosition.equals("Left Top")){
            drumPosition = "LT";
        }else if(drumPosition.equals("Middle Top")){
            drumPosition = "CT";
        }else if(drumPosition.equals("Middle Bottom")){
            drumPosition = "CD";
        }else if(drumPosition.equals("Right Bottom")){
            drumPosition = "RD";
        }else if(drumPosition.equals("Left Bottom")){
            drumPosition = "LD";
        }else if(drumPosition.equals("Right Top")){
            drumPosition = "RT";
        }
        
        setDrum.put(drumPosition,drumSound);
        System.err.println(setDrum);
       
        removeDrumPositionNameFromList(buttonTitle1);
        
        javax.swing.JButton drumSoundButton = new javax.swing.JButton();
        drumSoundButton.setBackground(new java.awt.Color(124, 221, 221));
        drumSoundButton.setText(buttonTitle1 + ": " + buttonTitle2);
        drumSoundButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list1MouseClicked(evt);
            }
        });
        drumSoundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list1ActionPerformed(evt);
            }
        });
        
        return drumSoundButton;
    }
    
    // For re-arrange the buttons, remove all buttons and add buttons to panel
    public void rearrangeDrumSoundButton () {
        removeAllDrumSoundButton();
        addAllDrumSoundButton(); 
        
        // Re-paint the panel
        jPanel5.revalidate();
        jPanel5.repaint();  
    }
    
   
    
    public void removeDrumSoundButton () {
        
        // Go out the method if user did't select a button yet
        if (selectedDrumSoundButtonIndex == -1) {
            return;
        }
        
        // Get the button the user selected
        javax.swing.JButton lastButton = drumSoundButtonList.get(selectedDrumSoundButtonIndex);
        
        // Remove the button from panel
        jPanel5.remove(lastButton);
        
        // Remove the button from list
        drumSoundButtonList.remove(selectedDrumSoundButtonIndex);
        
        // if removing success, assign -1 selectedDrumSoundButtonIndex
        selectedDrumSoundButtonIndex = -1;
    }
    
    public void removeAllDrumSoundButton () {
        for (int i=0 ; i<drumSoundButtonList.size() ; i++) {
            jPanel5.remove(drumSoundButtonList.get(i));
        }
    }
    
    public void addAllDrumSoundButton () {
        for (int i=0 ; i<drumSoundButtonList.size() ; i++) {
            jPanel5.add(drumSoundButtonList.get(i), new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10 + (45*i), 180, 40));
        }
    }
    
    public void shouldDisableSetButton () {
        if (drumSoundButtonList.size() >= 6) {
            set.setEnabled(false);
        } else {
            set.setEnabled(true);
        }
    }
    
    private void list1MouseClicked(java.awt.event.MouseEvent evt) {                                   
        selectedDrumSoundButtonIndex = evt.getComponent().getLocation().y/45;
               
        remove.setEnabled(true);
    }          
    
    private void list1ActionPerformed(java.awt.event.ActionEvent evt) {                                      

    }  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        startServer = new javax.swing.JButton();
        disconnect = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        position = new javax.swing.JComboBox();
        position = new javax.swing.JComboBox();
        sound = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        set = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        textField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        IPConnect = new javax.swing.JTextArea();
        speaker = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        startServer.setBackground(new java.awt.Color(204, 204, 204));
        startServer.setText("Start");
        startServer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startServerMouseClicked(evt);
            }
        });
        startServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startServerActionPerformed(evt);
            }
        });

        disconnect.setText("Stop");
        disconnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                disconnectMouseClicked(evt);
            }
        });
        disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectActionPerformed(evt);
            }
        });

        remove.setText("Remove");
        remove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeMouseClicked(evt);
            }
        });
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(113, 212, 212));

        position.setModel(new javax.swing.DefaultComboBoxModel(drumPositionName.toArray(new String[drumPositionName.size()])));
        position.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionActionPerformed(evt);
            }
        });

        sound.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "bongo1", "bongo2", "cowbell", "cymbal", "knock", "tomba1", "tomba2" }));
        sound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundActionPerformed(evt);
            }
        });

        jLabel1.setText("Add Drum");

        jLabel3.setText("Set Drum Sound");

        set.setText("Set");
        set.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setMouseClicked(evt);
            }
        });
        set.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(32, 32767));

        jLabel2.setText("Device Connected");

        textField.setText(null);
        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldActionPerformed(evt);
            }
        });

        sendButton.setText("send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the textField from the text view
                String messageText = textField.getText();
                // add textField to the textField area

                messageArea.append("\n"+"Server: " + messageText);

                // send the textField to the client
                try {
                    sendMessage(messageText);
                } catch (Exception ex) {

                }
                // clear text
                textField.setText("");
            }

        });
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        messageArea.setColumns(30);
        messageArea.setRows(10);
        messageArea.setBorder(null);
        messageArea.setEditable(false);
        jScrollPane1.setViewportView(messageArea);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton)))
        );

        IPConnect.setColumns(20);
        IPConnect.setRows(5);
        jScrollPane3.setViewportView(IPConnect);
        //IPConnect.append(AcceptConnection.x);
        //IPConnect.append("\neiei");

        speaker.setIcon(new javax.swing.ImageIcon("/Users/athitiya/Desktop/Drum/src/drum/p_2.png")); // NOI18N
        speaker.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                speakerMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 542, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(set, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sound, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(position, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(145, 145, 145))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(speaker)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(speaker, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(position, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(set)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drum/p_1.jpg"))); // NOI18N

        jPanel5.setBackground(new java.awt.Color(68, 189, 189));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(startServer, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(disconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startServer, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
 
    private void startServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startServerActionPerformed
        try {
            boolean initSucceed = initSocket(); // Start server
            if (initSucceed) {
                addConnectStatus();
                textField.setEnabled(true);
                sendButton.setEnabled(true);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startServerActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        int p = JOptionPane.showConfirmDialog(null,"Do you rally want to remove?","Delete",JOptionPane.YES_NO_OPTION);
        if(p ==0){
        // Remove the button with specific index from panel
        removeDrumSoundButton();
        
        // Refresh the panel
        rearrangeDrumSoundButton();
        
        // Check the enabled of set button
        shouldDisableSetButton();
         }
    }//GEN-LAST:event_removeActionPerformed

    private void setActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setActionPerformed
      setDrumSoundButton();
      
    }//GEN-LAST:event_setActionPerformed

    private void positionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_positionActionPerformed

    private void disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectActionPerformed
        try {
            closeSocket();
            
            textField.setEnabled(false);
            sendButton.setEnabled(false);
        } catch (IOException ex) {
            Logger.getLogger(NewFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_disconnectActionPerformed

    private void removeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeMouseClicked
        
    }//GEN-LAST:event_removeMouseClicked

    private void startServerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startServerMouseClicked
        startServer.setEnabled(false);
        disconnect.setEnabled(true);
    }//GEN-LAST:event_startServerMouseClicked

    private void disconnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_disconnectMouseClicked
        startServer.setEnabled(true);
        disconnect.setEnabled(false);
        
        while (disconnect.isEnabled() == false){
            int p = JOptionPane.showConfirmDialog(null,"Do you rally want to stop?","Yes",JOptionPane.YES_NO_OPTION);
            if(p == 0){
                addDisconnectStatus();
            }
        break;}
    }//GEN-LAST:event_disconnectMouseClicked

    private void setMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setMouseClicked

       
    }//GEN-LAST:event_setMouseClicked

    private void setMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_setMousePressed

    private void soundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_soundActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sendButtonActionPerformed

    private void textFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldActionPerformed

    private void speakerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_speakerMouseClicked
        speaker.setIcon(new javax.swing.ImageIcon("/Users/athitiya/Desktop/Drum/src/drum/p_3.png"));
    }//GEN-LAST:event_speakerMouseClicked

    public void MidiHandler(Server server){
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    Server server = new Server();
                    NewFrame midiHandler = new NewFrame(server);
                    midiHandler.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(NewFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea IPConnect;
    private javax.swing.JButton disconnect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JComboBox position;
    private javax.swing.JButton remove;
    private javax.swing.JButton sendButton;
    private javax.swing.JButton set;
    private javax.swing.JComboBox sound;
    private javax.swing.JLabel speaker;
    private javax.swing.JButton startServer;
    private javax.swing.JTextField textField;
    // End of variables declaration//GEN-END:variables
}
