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
    public void saveChats();
    public void saveChat(String nick);
    public void sendMessagePublic(Friend newFriend, String msg);
    public void sendMessagePrivate(Friend author, Friend receiver, String msg);
    //Getters
    public List<String> getChat(String nick);
    public Friend getReceiever();
    public Friend getChatUser();
    //Setters
    public void setReciever(String newReciever);
    public void setChatUser(String newUser);
    //Booleans
    public boolean isChatLoaded(String nick);
}
