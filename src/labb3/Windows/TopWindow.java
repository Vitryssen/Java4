/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb3.Windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author André
 */
public class TopWindow extends ComponentAdapter{
    private JPanel showPanel = new JPanel();
    private JPanel top = new JPanel(new GridBagLayout());
    public JButton showButton = new JButton("Show");
    public JButton fileButton = new JButton("File");
    public GridBagConstraints c = new GridBagConstraints();
    private JPanel exitPanel = new JPanel();
    private JCheckBox privateButton = new JCheckBox("Private chat");
    private JCheckBox publicButton = new JCheckBox("Public chat");
    public TopWindow(){
        Border blackline;
        blackline = BorderFactory.createLineBorder(Color.black);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        top.add(fileButton , c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(0,0,0,300);  //top padding
        c.gridx = 1;       //aligned with button 2
        c.gridy = 0;       //third row
        //showButton.addActionListener(actionPerformed());
        top.add(showButton, c);

        JButton exitButton = new JButton("Exit");

        //Change show and exitPanel to place and resize
        //correctly with the file and show buttons
        showPanel.setBorder(blackline);
        showPanel.setVisible(false);

        exitPanel.setBorder(blackline);
        exitPanel.add(exitButton, BorderLayout.WEST);
        exitButton.setPreferredSize(new Dimension(90,30));
        exitPanel.setVisible(false);

        // add to a container
        showPanel.add(privateButton, BorderLayout.NORTH);
        showPanel.add(publicButton, BorderLayout.SOUTH);
        
        fileButton.addActionListener((ActionEvent e) -> {
            if(exitPanel.isVisible())
                exitPanel.setVisible(false);
            else
                exitPanel.setVisible(true);
        });

        exitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        showButton.addActionListener((ActionEvent e) -> {
            if(showPanel.isVisible())
                showPanel.setVisible(false);
            else
                showPanel.setVisible(true);
        });
        top.addComponentListener(new ComponentAdapter() {
        @Override
            public void componentResized(ComponentEvent componentEvent) {
                c.insets = new Insets(0,0,0, (int) (0.70*top.getWidth()));  //top padding
                top.add(showButton, c);
                exitPanel.setBounds(fileButton.getLocation().x, fileButton.getLocation().y+fileButton.getHeight(), fileButton.getWidth(), 50);
                showPanel.setBounds(showButton.getLocation().x, showButton.getLocation().y+showButton.getHeight(), showButton.getWidth(), 50);
                privateButton.setPreferredSize(new Dimension(showPanel.getWidth()-5,15));
                publicButton.setPreferredSize(new Dimension(showPanel.getWidth()-5,15));
                exitPanel.repaint();
                showPanel.repaint();
            }
        });
    }
    public JPanel getWindow(){
        return top;
    }
    public JPanel getShowPanel(){
        return showPanel;
    }
    public JPanel getExitPanel(){
        return exitPanel;
    }
    public JCheckBox getPublicButton(){
        return publicButton;
    }
    public JCheckBox getPrivateButton(){
        return privateButton;
    }
}
