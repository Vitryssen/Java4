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
    private String chatUser = "Eurakarte";
    private String chattingWith = "";
    private final List<Friend> friends = new ArrayList<>();
    private List<Message> msgs;
    private final Chat allChats = new Chat(chatUser);
    public ChatDAOImp(){
        
    }
    public void newFriend(String newFriend){
        Friend currentFriend = new Friend();
        String start = newFriend.substring(newFriend.indexOf("<", newFriend.indexOf("<")+2)+1);
        
        currentFriend.setNick(start.substring(0, start.indexOf(">")));
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getNick().equals(currentFriend.getNick()))
                return;
        }
        start = start.substring(start.indexOf(">")+2);
        currentFriend.setName(start.substring(0, start.indexOf(">")));
        start = start.substring(start.indexOf(">")+2);
        currentFriend.setIp(start.substring(0, start.indexOf(">")));
        start = start.substring(start.indexOf(">")+2);
        currentFriend.setImage(start.substring(0, start.indexOf(">")));
        
        friends.add(currentFriend);
        //currentFriend.print();
    }
    @Override
    public void saveChats(){
        Map<String, List<Message>> chats = allChats.getAllChats();
        for(String key: chats.keySet()){
            Friend newFriend = new Friend();
            newFriend.setNick(chatUser);
            for(int i = 0; i < friends.size(); i++){
                if(friends.get(i).getNick().equals(key))
                    newFriend = friends.get(i);
            }
            new LogWriter(newFriend, chats.get(key));
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
            //System.out.println(friendList.getFriendList().get(i).getNick());
            String text = friends.get(i).getNick()+friends.get(i).getTag();
            AffineTransform affinetransform = new AffineTransform();     
            FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
            Font font = new Font("Tahoma", Font.PLAIN, 12);
            if( ((int)(font.getStringBounds(text, frc).getWidth())+5) > previous)
                previous = (int)(font.getStringBounds(text, frc).getWidth())+5;
        }
        return previous;
    }

    @Override
    public List<String> getPublicChat() {
        List<String> returnList = new ArrayList<>();
        msgs = allChats.getMessages();
        for(int i = 0; i < msgs.size(); i++){
            Friend author = msgs.get(i).getAuthor();
            String newString = "<"+author.getNick()+author.getTag()+"> "+msgs.get(i).getMessage()+"\n";
            returnList.add(newString);
        }
        return returnList;
    }
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
        this.chatUser = newUser;
    }
    @Override
    public void sendMessage(String msg){
        if(msg.length() > 0){
            Friend currentFriend = new Friend();
            currentFriend.setNick(chatUser);
            Message newMsg = new Message(currentFriend, msg);
            msgs.add(newMsg);
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
    public String getChatUser(){
        return chatUser;
    }
    @Override
    public boolean isChatLoaded(String nick){
        return allChats.chatExists(nick);
    }
}
