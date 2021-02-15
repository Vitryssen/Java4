/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.Main;

import labb4.IO.LogWriter;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
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
    private Friend chatUser = new Friend();
    private String chattingWith = "";
    private List<Friend> friends = new ArrayList<>();
    private List<Message> msgs;
    private final Chat allChats = new Chat(chatUser.getNick());
    public ChatDAOImp(){
        
    }
    @Override
    public void saveChats(){
        Map<String, List<Message>> chats = allChats.getAllChats();
        for(String key: chats.keySet()){
            Friend newFriend = new Friend();
            for(int i = 0; i < friends.size(); i++){
                if(friends.get(i).getNick().equals(key))
                    newFriend = friends.get(i);
            }
            new LogWriter(chatUser, chats.get(key));
        }
    }
    @Override
    public List<Friend> getAllFriends(){
        return friends;
    }
    @Override
    public int getFriend(String name){
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getNick().equals(name)){
                return i;
            }
        }
        return -1; //returns -1 if no match found
    }
    @Override
    public void changeFriendAttr(String user, String attribute, String newValue){
        int friendIndex = getFriend(user);
        if(friendIndex != -1){
            switch(attribute) {
                case "Nickname" -> {
                    if(allChats.chatExists(friends.get(friendIndex).getNick()))
                        allChats.changeChatNick(user, newValue);
                    friends.get(friendIndex).setNick(newValue);
                }
                case "Fullname" -> friends.get(friendIndex).setName(newValue);
                case "Image" -> friends.get(friendIndex).setImage(newValue);
              }
        }
    }
    @Override
    public int getLongestNick(){
        int previous = 0;
        for(int i = 0; i < friends.size(); i++){
            String text = friends.get(i).getNick()+friends.get(i).getTag();
            AffineTransform affinetransform = new AffineTransform();     
            FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
            Font font = new Font("Tahoma", Font.PLAIN, 12);
            if( ((int)(font.getStringBounds(text, frc).getWidth())+5) > previous)
                previous = (int)(font.getStringBounds(text, frc).getWidth())+5;
        }
        return previous;
    }

    /*@Override
    public List<String> getPublicChat() {
        List<String> returnList = new ArrayList<>();
        msgs = allChats.getMessages();
        for(int i = 0; i < msgs.size(); i++){
            Friend author = msgs.get(i).getAuthor();
            String newString = "";
            if(author.getNick().length() > 0)
                newString = "<"+author.getNick()+author.getTag()+"> "+msgs.get(i).getMessage()+"\n";
            returnList.add(newString);
        }
        return returnList;
    }*/
    @Override
    public List<String> getPrivateChat(String nick) { 
        List<String> returnList = new ArrayList<>();
        msgs = allChats.getMessages(nick);
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
    public void setFriendlist(List<Friend> newList){
        friends = newList;
        Collections.sort(friends, new Sortbynick());
    }
    @Override
    public void setPublicChat(List<String> newChat){
        if(allChats.chatExists(chatUser.getNick())){
            if(newChat.size() < allChats.getUserChat(chatUser.getNick()).size()){
                return;
            }
        }
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
}
