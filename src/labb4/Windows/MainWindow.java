/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.Windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.border.Border;
import labb4.DAO.ChatDAOImp;
import labb4.DataStructures.Friend;
import labb4.DAO.ChatDAO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import labb4.DAO.FriendDAO;
import labb4.DAO.FriendDAOImp;
import labb4.Socket.Client;
/**
 *
 * @author André
 */
public class MainWindow {
    //Main window
    private JFrame f;  
    //DAO's
    private ChatDAO chatDao = new ChatDAOImp();
    private FriendDAO friendDao = new FriendDAOImp();
    //Windows
    private TopWindow topWindow = new TopWindow();
    private ChatWindow chatWindow = new ChatWindow();
    private FriendWindow friendsWindow = new FriendWindow();
    //Components
    private JCheckBox publicButton = topWindow.getPublicButton();
    private JCheckBox privateButton = topWindow.getPrivateButton();
    //Variables
    boolean privateMode = false;
    //Network socket
    private Client readerThread = new Client("chatbot.miun.se", 8000);//Skolan : "chatbot.miun.se", 8000 hemma: "0.0.0.0", 8000
    public MainWindow() throws IOException{
        f=new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(topWindow.getWindow(), BorderLayout.NORTH);
        
        topWindow.getShowPanel().setBounds(111,45,110,70);
        topWindow.getExitPanel().setBounds(6,45,100,40);
        //----------------------------------------
        f.add(topWindow.getShowPanel());
        f.add(topWindow.getExitPanel());
        //----------------------------------------
        String nick = nickNameInput();
        chatDao.setChatUser(nick);
        //----------------------------------------
        readerThread.connect();
        readerThread.register(nick);
        readerThread.setPublicChat(chatDao.convertToServer());
        writeThread();
        //----------------------------------------
        addPublicClick();
        addPrivateClick();
        addSendChatClick();
        exitButtonEvent();
        //----------------------------------------
        JPanel bottom = new JPanel();
        bottom.add(chatWindow.getWindow(), BorderLayout.WEST);
        bottom.add(friendsWindow.getWindow(), BorderLayout.EAST);
        //-----------------------------------------
        Border blackline = BorderFactory.createLineBorder(Color.black);
        bottom.setBorder(blackline);
        f.setTitle(nick);
        f.add(bottom);
        f.pack();
        f.setVisible(true); 
        f.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                resize();
            }
        });
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }
    public void writeThread(){
        new Thread(new Runnable() {
            public void run() {
                while(true){
                    // Runs inside of the Swing UI thread
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            friendDao.setFriendlist(readerThread.getFriends());
                            populateFriendlist();
                            if(chatWindow.getChatLabel().getText() != "Not chatting"){
                                if(privateMode){
                                    loadPrivateChat();
                                }
                                else{
                                    chatDao.setPublicChat(readerThread.getPublicMessages());
                                    loadPublicChat();
                                }
                            }
                        }
                    });
                    try {
                        java.lang.Thread.sleep(100);
                    }
                    catch(Exception e) { 
                        System.out.println("Write Thread: "+e);
                    }
                }
            }
        }).start();
    }
    public void resize(){
        chatWindow.getWindow().setPreferredSize(new Dimension(f.getWidth()-friendDao.getLongestNick()-40, f.getHeight()-80));
        friendsWindow.getWindow().setPreferredSize(new Dimension(friendDao.getLongestNick()+10,f.getHeight()-80));
        f.revalidate();
        f.repaint();
    }
    private void exit(){
        chatDao.saveChats();
        readerThread.sendLogout();
        f.dispose();
        System.exit(0);
    }
    private void addSendChatClick(){
        chatWindow.getMessageButton().addMouseListener(new MouseAdapter() { 
            public void mousePressed(MouseEvent me) { 
                if(chatWindow.getChatLabel().getText() != "Not chatting" && chatWindow.getMessageInput().getText().length() > 0){
                    chatWindow.getChatText().setText("");
                    if(me.getButton() == 1 && privateMode){
                        chatDao.sendMessage(chatWindow.getMessageInput().getText());
                        readerThread.sendPrivateMessage(chatWindow.getMessageInput().getText(), chatDao.getReceiever());
                        loadPrivateChat();
                        chatWindow.getMessageInput().setText("");
                    }
                    else if(me.getButton() == 1 && !privateMode){
                        chatDao.setReciever(chatDao.getChatUser().getNick());
                        //chatDao.sendMessage(chatWindow.getMessageInput().getText());
                        readerThread.sendPublicMessage(chatWindow.getMessageInput().getText());
                        List<String> messages = chatDao.getChat(chatDao.getChatUser().getNick());
                        for(int i = 0; i < messages.size(); i++){
                            chatWindow.getChatText().append(messages.get(i));
                        }
                        chatWindow.getMessageInput().setText("");
                    }
                }
            } 
        });
    }
    public String nickNameInput(){
        String value = "";
        while(value.length() < 1){
            value = JOptionPane.showInputDialog(null, "Chose nickname", 
                        "Nickname: ", JOptionPane.INFORMATION_MESSAGE);
            if(value == null){
                value = "";
            }
            if(value.length() < 1)
                JOptionPane.showMessageDialog(f, "Nickname cannot by empty");
        }
        return value;
    }
    private void popUp(String user){
        String[] options = {"Fullname","Image"};
        String attr = (String)JOptionPane.showInputDialog(null, "What attribute do you want to change?", 
                "Change attribute for "+user, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if(attr != null){
            if(!chatDao.isChatLoaded(user)){
                JOptionPane.showMessageDialog(f,"Please load the chat atleast once before changing nick","Alert",JOptionPane.WARNING_MESSAGE);
                return;
            }
            String value = JOptionPane.showInputDialog(null, attr+": ", 
                    "New value for "+attr, JOptionPane.INFORMATION_MESSAGE);
            if(value.length() == 0)
                return;
            friendDao.changeFriendAttr(user, attr, value);
        }
    }
    private void populateFriendlist(){
        friendsWindow.getNamePanel().removeAll();
        for(int i = 0; i < friendDao.getAllFriends().size(); i++){
            Friend currentFriend = friendDao.getAllFriends().get(i);
            String currentName = currentFriend.getNick();
            JLabel nameLabel = new JLabel(currentName+currentFriend.getTag());
            nameLabel.setName(currentName);
            friendsWindow.getNamePanel().add(nameLabel, BorderLayout.WEST);
        }
        addClickListiner();
        resize();
    }
    private void addClickListiner(){
        for(int i = 0; i < friendsWindow.getNamePanel().getComponentCount(); i++){
            friendsWindow.getNamePanel().getComponent(i).addMouseListener(new MouseAdapter() { 
                public void mousePressed(MouseEvent me) { 
                    if(me.getButton() == 3){
                        popUp(me.getComponent().getName());
                    }
                    else if(privateMode == true && me.getButton() == 1){
                        chatDao.setReciever(me.getComponent().getName());
                        JLabel labelText = (JLabel) me.getComponent();
                        chatWindow.getChatLabel().setText("Chatting with "+labelText.getText());
                        chatWindow.getChatText().setText("");
                        loadPrivateChat();
                    }
                } 
            });
        }
    }
    private void loadPrivateChat(){
        chatWindow.getChatText().setText("");
        List<String> history = chatDao.getChat(chatDao.getReceiever());
        for(int i = 0; i < history.size(); i++){
            chatWindow.getChatText().append(history.get(i));
        }
    }
    private void loadPublicChat(){
        chatWindow.getChatText().setText("");
        if(publicButton.isSelected()){
            chatDao.setReciever(chatDao.getChatUser().getNick());
            chatWindow.getChatLabel().setText("Chatting publicly");
            List<String> messages = chatDao.getChat(chatDao.getReceiever());
            for(int i = 0; i < messages.size(); i++){
                chatWindow.getChatText().append(messages.get(i));
            }
        }
    }
    private void addPublicClick(){
        publicButton.addActionListener((ActionEvent e) -> { 
            chatWindow.getChatLabel().setText("Not chatting");
            chatWindow.getChatText().setText("");
            loadPublicChat();
            privateButton.setSelected(false);
            privateMode = false;
            topWindow.getShowPanel().setVisible(false);
            topWindow.getExitPanel().setVisible(false);
        });
    }
    private void addPrivateClick(){
        privateButton.addActionListener((ActionEvent e) -> {
            privateMode = true;
            publicButton.setSelected(false);
            chatWindow.getChatLabel().setText("Not chatting");
            chatWindow.getChatText().setText("");
            topWindow.getShowPanel().setVisible(false);
            topWindow.getExitPanel().setVisible(false);
        });
    }
    private void exitButtonEvent(){
        topWindow.getExitButton().addActionListener((ActionEvent e) -> {
            exit();
        });
    }
}
