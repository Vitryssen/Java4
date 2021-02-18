/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.DAO;

import java.util.List;
import labb4.DataStructures.Friend;

/**
 *
 * @author André
 */
public interface ChatDAO {
    //Functions
    public void sendMessagePublic(Friend newFriend, String msg);
    public void sendMessagePrivate(Friend newFriend, String msg);
    public void saveChats();
    public List<String> convertToServer();
    //Getters
    public List<String> getChat(String nick);
    public String getReceiever();
    public Friend getChatUser();
    //Setters
    public void setReciever(String newReciever);
    public void setChatUser(String newUser);
    public void setPublicChat(List<String> newChat);
    public void setPrivateChat(List<String> newChat);
    //Booleans
    public boolean isChatLoaded(String nick);
}
