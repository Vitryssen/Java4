/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.DAO;

import labb4.IO.LogWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import labb4.DataStructures.Chat;
import labb4.DataStructures.Friend;
import labb4.DataStructures.Message;

/**
 *
 * @author André
 */
public class ChatDAOImp implements ChatDAO{
    private FriendDAO friendDao = new FriendDAOImp();
    private Friend chatUser = new Friend();
    private String chattingWith = "";
    private final Chat allChats = new Chat(chatUser.getNick());
    public ChatDAOImp(){
        
    }
    @Override
    public void saveChats(){
        Map<String, List<Message>> chats = allChats.getAllChats();
        for(String key: chats.keySet()){
            Friend newFriend = new Friend();
            for(int i = 0; i < friendDao.getAllFriends().size(); i++){
                if(friendDao.getAllFriends().get(i).getNick().equals(key))
                    newFriend = friendDao.getAllFriends().get(i);
            }
            if(newFriend.getNick().length() < 1)
                new LogWriter(chatUser, chats.get(key));
            else
                new LogWriter(newFriend, chats.get(key));
        }
    }
    @Override
    public List<String> getChat(String nick) { 
        List<String> returnList = new ArrayList<>();
        List<Message> msgs = allChats.getMessages(nick);
        for(int i = 0; i < msgs.size(); i++){
            Friend author = msgs.get(i).getAuthor();
            String newString = "";
            if(author.getNick().length() > 0)
               newString = "<"+author.getNick()+author.getTag()+"> "+msgs.get(i).getMessage()+"\n";
            returnList.add(newString);
        }
        return returnList;
    }
    @Override
    public void setChatUser(String newUser) {
        this.chatUser.setNick(newUser);
    }
    @Override
    public void sendMessage(String msg){
        if(msg.length() > 0){
            Message newMsg = new Message(chatUser, msg);
            allChats.addMessage(newMsg, chattingWith);
        }
    }
    @Override
    public void setReciever(String newReciever){
        chattingWith = newReciever;
    }
    @Override
    public String getReceiever(){
        return chattingWith;
    }
    @Override
    public Friend getChatUser(){
        return chatUser;
    }
    @Override
    public boolean isChatLoaded(String nick){
        return allChats.chatExists(nick);
    }
    @Override
    public void setPublicChat(List<String> newChat){
        List<Message> newList = new ArrayList<>();
        for(int i = 0; i < newChat.size(); i++){
            Friend newFriend = new Friend();
            String temp = newChat.get(i);
            temp = temp.substring(temp.indexOf(">")+2);
            newFriend.setNick(temp.substring(0, temp.indexOf(">")));
            temp = temp.substring(temp.indexOf(">")+2);
            Message newMsg = new Message(newFriend, temp.substring(0, temp.indexOf(">")));
            newList.add(newMsg);
        }
        allChats.setChat(chatUser.getNick(), newList);
    }
    public List<String> convertToServer(){
        String publicMessage = "<PUBLIC><%1$s><%2$s>";
        List<Message> temp = allChats.getMessages(chatUser.getNick());
        List<String> newList = new ArrayList<>();
        String message;
        for(int i = 0; i < temp.size(); i++){
            message = String.format(publicMessage, temp.get(i).getAuthor().getNick(), temp.get(i).getMessage());
            newList.add(message);
        }
        return newList;
    }
}
