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
import labb4.Main.ChatDAOImp;
import labb4.DataStructures.Friend;
import labb4.Main.ChatDAO;
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
import labb4.DataStructures.Message;
import labb4.Socket.Client;
/**
 *
 * @author André
 */
public class MainWindow {
    private JFrame f;  
    private ChatDAO chatDao = new ChatDAOImp();
    private TopWindow top = new TopWindow();
    private ChatWindow chat = new ChatWindow();
    private FriendWindow friends = new FriendWindow();
    boolean privateMode = false;
    private JCheckBox publicButton = top.getPublicButton();
    private JCheckBox privateButton = top.getPrivateButton();
    private Client test = new Client("chatbot.miun.se", 8000);
    public MainWindow() throws IOException{
        f=new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(top.getWindow(), BorderLayout.NORTH);
        
        top.getShowPanel().setBounds(111,45,110,70);
        top.getExitPanel().setBounds(6,45,100,40);
        //----------------------------------------
        f.add(top.getShowPanel());
        f.add(top.getExitPanel());
        //----------------------------------------
        String nick = nickNameInput();
        chatDao.setChatUser(nick);
        test.connect();
        test.register(nick);
        FriendsThread();
        PublicThread();
        //----------------------------------------
        addClickListiner();
        addPublicClick();
        addPrivateClick();
        addSendChatClick();
        exitButtonEvent();
        //----------------------------------------
        JPanel bottom = new JPanel();
        bottom.add(chat.getWindow(), BorderLayout.WEST);
        bottom.add(friends.getWindow(), BorderLayout.EAST);
        //-----------------------------------------
        Border blackline = BorderFactory.createLineBorder(Color.black);
        bottom.setBorder(blackline);
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
    private void exit(){
        chatDao.saveChats();
        test.sendLogout();
        f.dispose();
        System.exit(0);
    }
    private void addSendChatClick(){
        chat.getMessageButton().addMouseListener(new MouseAdapter() { 
            public void mousePressed(MouseEvent me) { 
                if(chat.getChatLabel().getText() != "Not chatting" && chat.getMessageInput().getText().length() > 0){
                    chat.getChatText().setText("");
                    if(me.getButton() == 1 && privateMode){
                        chatDao.sendMessage(chat.getMessageInput().getText());
                        test.sendPrivateMessage(chat.getMessageInput().getText(), chatDao.getReceiever());
                        loadPrivateChat();
                        chat.getMessageInput().setText("");
                    }
                    else if(me.getButton() == 1 && !privateMode){
                        chatDao.setReciever(chatDao.getChatUser().getNick());
                        chatDao.sendMessage(chat.getMessageInput().getText());
                        test.sendPublicMessage(chat.getMessageInput().getText());
                        List<String> messages = chatDao.getPrivateChat(chatDao.getChatUser().getNick());
                        for(int i = 0; i < messages.size(); i++){
                            chat.getChatText().append(messages.get(i));
                        }
                        chat.getMessageInput().setText("");
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
    public void FriendsThread() throws IOException{
        Thread thread = new Thread() {
            @Override
            public void run(){
              while(true){
                  try {
                      Thread.sleep(1000);
                      chatDao.setFriendlist(test.getFriends());
                      populateFriendlist();
                      addClickListiner();
                  } catch (InterruptedException ex) {
                      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }
            }
         };
        thread.start();
    }
    public void PublicThread() throws IOException{
        Thread thread = new Thread() {
            @Override
            public void run(){
              while(true){
                  try {
                        Thread.sleep(1000);
                        chatDao.setPublicChat(test.getPublicMessages());
                        loadPublicChat();
                  } catch (InterruptedException ex) {
                      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }
            }
         };
        thread.start();
    }
    public void resize(){
        chat.getWindow().setPreferredSize(new Dimension(f.getWidth()-chatDao.getLongestNick()-50, f.getHeight()-80));
        friends.getWindow().setPreferredSize(new Dimension(chatDao.getLongestNick()+10,f.getHeight()-80));
        f.repaint();
    }
    private void popUp(String user){
        String[] options = {"Fullname","Image"};
        String attr = (String)JOptionPane.showInputDialog(null, "What attribute do you want to change?", 
                "Change attribute for "+user, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if(attr != null){
            if(!chatDao.isChatLoaded(user)){
                JOptionPane.showMessageDialog(f,"Please load the chat atleast once before changing nick","Alert",JOptionPane.WARNING_MESSAGE);
                return;
            }
            String value = JOptionPane.showInputDialog(null, attr+": ", 
                    "New value for "+attr, JOptionPane.INFORMATION_MESSAGE);
            if(value.length() == 0)
                return;
            chatDao.changeFriendAttr(user, attr, value);
            if("Nickname".equals(attr)){
                friends.getNamePanel().removeAll();
                populateFriendlist();
                addClickListiner();
                friends.getNamePanel().revalidate();
                if(privateMode){
                    chatDao.setReciever(value);
                    loadPrivateChat();
                    chat.getChatLabel().setText("Chatting with "+chatDao.getAllFriends().get(chatDao.getFriend(value)).getNick()+chatDao.getAllFriends().get(chatDao.getFriend(value)).getTag());
                }
            }
        }
    }
    private void populateFriendlist(){
        friends.getNamePanel().removeAll();
        for(int i = 0; i < chatDao.getAllFriends().size(); i++){
            Friend currentFriend = chatDao.getAllFriends().get(i);
            String currentName = currentFriend.getNick();
            JLabel nameLabel = new JLabel(currentName+currentFriend.getTag());
            nameLabel.setName(currentName);
            friends.getNamePanel().add(nameLabel, BorderLayout.WEST);
        }
        resize();
        friends.getNamePanel().revalidate();
        friends.getNamePanel().repaint();
    }
    private void addClickListiner(){
        for(int i = 0; i < friends.getNamePanel().getComponentCount(); i++){
            friends.getNamePanel().getComponent(i).addMouseListener(new MouseAdapter() { 
                public void mousePressed(MouseEvent me) { 
                    if(me.getButton() == 3){
                        popUp(me.getComponent().getName());
                    }
                    else if(privateMode == true && me.getButton() == 1){
                        chatDao.setReciever(me.getComponent().getName());
                        JLabel labelText = (JLabel) me.getComponent();
                        chat.getChatLabel().setText("Chatting with "+labelText.getText());
                        chat.getChatText().setText("");
                        loadPrivateChat();
                    }
                } 
            });
        }
    }
    private void loadPrivateChat(){
        chat.getChatText().setText("");
        List<String> history = chatDao.getPrivateChat(chatDao.getReceiever());
        for(int i = 0; i < history.size(); i++){
            chat.getChatText().append(history.get(i));
        }
    }
    private void loadPublicChat(){
        chat.getChatText().setText("");
        if(publicButton.isSelected()){
            chatDao.setReciever(chatDao.getChatUser().getNick());
            chat.getChatLabel().setText("Chatting publicly");
            List<String> messages = chatDao.getPrivateChat(chatDao.getChatUser().getNick());
            for(int i = 0; i < messages.size(); i++){
                chat.getChatText().append(messages.get(i));
            }
        }
    }
    private void addPublicClick(){
        publicButton.addActionListener((ActionEvent e) -> { 
            chat.getChatLabel().setText("Not chatting");
            chat.getChatText().setText("");
            loadPublicChat();
            privateButton.setSelected(false);
            privateMode = false;
            top.getShowPanel().setVisible(false);
            top.getExitPanel().setVisible(false);
        });
    }
    private void addPrivateClick(){
        privateButton.addActionListener((ActionEvent e) -> {
            privateMode = !privateMode;
            chat.getChatLabel().setText("Not chatting");
            publicButton.setSelected(false);
            chat.getChatText().setText("");
            top.getShowPanel().setVisible(false);
            top.getExitPanel().setVisible(false);
        });
    }
    private void exitButtonEvent(){
        top.getExitButton().addActionListener((ActionEvent e) -> {
            exit();
        });
    }
}
