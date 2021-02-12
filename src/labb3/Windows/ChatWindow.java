/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb3.Windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
/**
 *
 * @author André
 */
public class ChatWindow extends JPanel{
    private final JPanel chat = new JPanel();
    private final JTextArea chatText = new JTextArea("");
    private final JPanel messagePanel = new JPanel();
    private final JLabel chatLabel = new JLabel("Not chatting");
    private final JTextField messageInput = new JTextField();
    private final JButton sendChat = new JButton("Send");
    public ChatWindow(){
        Border blackline = BorderFactory.createLineBorder(Color.black);
        
        chat.setLayout(new BorderLayout());

        chatText.setLineWrap(true);
        chatText.setWrapStyleWord(true);
        chatText.setBorder(blackline);
        chatText.setEditable(false);
        chatText.setText("\n\n\n\n\n\n");
        chat.add(chatLabel, BorderLayout.NORTH); 
        
        JScrollPane scroll = new JScrollPane(chatText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chat.add(scroll);
        
        JPanel messagePanel = new JPanel();
        messagePanel.add(messageInput, BorderLayout.WEST);
        messageInput.setPreferredSize(new Dimension(200, 20));
        messagePanel.add(sendChat, BorderLayout.EAST);
        chat.add(messagePanel, BorderLayout.SOUTH);
    }
    public JTextField getMessageInput(){
        return messageInput;
    }
    public JPanel getWindow(){
        return chat;
    }
    public JTextArea getChatText(){
        return chatText;
    }
    public JLabel getChatLabel(){
        return chatLabel;
    }
    public JPanel getMessagePanel(){
        return messagePanel;
    }
    public JButton getMessageButton(){
        return sendChat;
    }
}
