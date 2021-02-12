/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb3.DataStructures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import labb3.Readers.LogReader;
import labb3.LogWriter;

/**
 *
 * @author André
 */
public class Chat {
    private String author;
    private Map<String, List<Message>> userChats = new HashMap<String, List<Message>>(); 
    public Chat(String nickname){
      this.author = nickname;
    }
    public void changeChatNick(String oldNick, String newNick){
        if(userChats.containsKey(oldNick)){
            userChats.put( newNick, userChats.remove( oldNick ) );
            changeNickInChat(oldNick, newNick);
        }
    }
    public void addMessage(Message msg){
        new LogWriter(msg); //Fix better 
    }
    public List<Message> getMessages(){
        if(userChats.containsKey(author)){
            return userChats.get(author);
        }
        else{
            LogReader reader = new LogReader();
            reader.readFile(author);
            userChats.put(author, reader.getChats().get(author));
            return userChats.get(author);
        }
    }
    public List<Message> getMessages(String privateName){
        if(userChats.containsKey(privateName)){
            return userChats.get(privateName);
        }
        else{
            LogReader reader = new LogReader();
            reader.readFile(privateName);
            userChats.put(privateName, reader.getChats().get(privateName));
            return userChats.get(privateName);
        }
    }
    private void changeNickInChat(String oldNick, String newNick){
        List<Message> oldMsgs = userChats.get(newNick);
        for(int i = 0; i < oldMsgs.size(); i++){
            if(oldMsgs.get(i).getAuthor().getNick().equals(oldNick)){                
                oldMsgs.get(i).getAuthor().setNick(newNick);
            }
        }
        userChats.remove(newNick);
        userChats.put(newNick, oldMsgs);
    }
    public boolean chatExists(String nickname){
        return userChats.containsKey(nickname);
    }
    public List<Message> getUserChat(String nickname){
        return userChats.get(nickname); //Returns chat to given nickname
    }
}
