/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import labb4.IO.LogReader;

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
    public Map<String, List<Message>> getAllChats(){
        return userChats;
    }
    public void setChat(String nick, List<Message> newChat){
        if(userChats.get(nick).equals(newChat)){
            return;
        }
        userChats.remove(nick);
        userChats.put(nick, newChat);
    }
    public void addMessage(Message msg, String recipient){
        List<Message> tempList = new ArrayList<>();
        if(chatExists(recipient)){
            tempList = userChats.get(recipient);
            tempList.add(msg);
            userChats.remove( recipient );
            userChats.put( recipient, tempList);
        }
        else{
            tempList.add(msg);
            userChats.put(recipient, tempList);
        }
    }
    public void changeChatNick(String oldNick, String newNick){
        if(userChats.containsKey(oldNick)){
            userChats.put( newNick, userChats.remove( oldNick ) );
            changeNickInChat(oldNick, newNick);
        }
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
