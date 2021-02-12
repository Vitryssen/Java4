/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import labb3.DataStructures.Message;

/**
 *
 * @author André
 */
public class LogWriter {
    public LogWriter(Message msg){
        String workingPath = System.getProperty("user.dir");
        File file=new File(workingPath+"\\logs\\"+msg.getAuthor()+".log");  
        try 
        {
            Files.write(Paths.get("myfile.txt"), "the text".getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) 
        {
            
        }
    }
}
