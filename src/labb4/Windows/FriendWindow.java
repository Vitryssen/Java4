/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.Windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import labb4.DAO.ChatDAOImp;
import labb4.DAO.ChatDAO;
import labb4.DAO.FriendDAO;
import labb4.DAO.FriendDAOImp;

/**
 *
 * @author André
 */
public class FriendWindow extends JPanel {
    private FriendDAO friendDao = new FriendDAOImp();
    private JPanel friends = new JPanel();
    private JPanel namePanel = new JPanel();
    public FriendWindow(){
        Border blackline;
        blackline = BorderFactory.createLineBorder(Color.black);
        friends.setLayout(new BorderLayout());
        
        namePanel.setBorder(blackline);
        namePanel.setBackground(Color.white);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        
        JLabel friendText = new JLabel("Friends list");
        
        friends.add(friendText, BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(namePanel);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        friends.add(scroll);
        friends.setPreferredSize(new Dimension(friendDao.getLongestNick()+10, 200)); //width determined by the longest name
    }
    public JPanel getWindow(){
        return friends;
    }
    public JPanel getNamePanel(){
        return namePanel;
    }
}
