/*
 * André Nordlund
 * 2021-02-12
 * Java 2
 * Lab 4
 */
package labb4.DAO;

import java.util.List;
import labb4.DataStructures.Friend;

/**
 *
 * @author André
 */
public interface FriendDAO {
    //Functions
    public void changeFriendAttr(String user, String attribute, String newValue);
    //Getters
    public int getLongestNick();
    public int getIndexOfFriend(String name);
    public List<Friend> getAllFriends();
    public Friend getFriend(String name);
    //Setters
    public void setFriendlist(List<Friend> newList);
}
