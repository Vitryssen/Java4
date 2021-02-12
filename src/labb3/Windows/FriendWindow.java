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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import labb3.ChatDAOImp;
import labb3.ChatDAO;

/**
 *
 * @author André
 */
public class FriendWindow extends JPanel {
    ChatDAO chatDao = new ChatDAOImp();
    private JPanel friends = new JPanel();
    private JPanel namePanel = new JPanel();
    public FriendWindow(){
        Border blackline;
        blackline = BorderFactory.createLineBorder(Color.black);
        friends.setLayout(new BorderLayout());
        
        namePanel.setBorder(blackline);
        namePanel.setBackground(Color.white);
        JLabel friendText = new JLabel("Friends list");
        
        friends.add(friendText, BorderLayout.NORTH);
        friends.add(namePanel, BorderLayout.CENTER);
        friends.setPreferredSize(new Dimension(chatDao.getLongestNick()+10, 200)); //width determined by the longest name
    }
    public JPanel getWindow(){
        return friends;
    }
    public JPanel getNamePanel(){
        return namePanel;
    }
}
