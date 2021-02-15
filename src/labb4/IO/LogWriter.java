/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import labb4.DataStructures.Friend;
import labb4.DataStructures.Message;

/**
 *
 * @author André
 */
public class LogWriter {
    public LogWriter(Friend nick, List<Message> msg){
        String workingPath = System.getProperty("user.dir");
        File file=new File(workingPath+"\\logs\\"+nick.getNick()+nick.getTag()+".log");  
        try {
            if (file.createNewFile()){
                System.out.println("File is created!");
            }
            else{
                System.out.println("File already exists.");
            }
            FileWriter fr = new FileWriter(file, false);
            for(int i = 0; i < msg.size(); i++){
                System.out.println("writing to file"+" "+nick.getNick());
                System.out.println("<"+msg.get(i).getAuthor().getNick()+msg.get(i).getAuthor().getTag()+">"+msg.get(i).getMessage());
                fr.write("<"+msg.get(i).getAuthor().getNick()+msg.get(i).getAuthor().getTag()+">"+msg.get(i).getMessage()+"\n");
            }
            fr.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
