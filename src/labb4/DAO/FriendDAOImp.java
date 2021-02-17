/*
 * André Nordlund
 * 2021-02-12
 * Java 2
 * Lab 4
 */
package labb4.DAO;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import labb4.DataStructures.Friend;
import labb4.Main.Sortbynick;

/**
 *
 * @author André
 */
public class FriendDAOImp implements FriendDAO{
    private List<Friend> friends = new ArrayList<>();
    
    @Override
    public List<Friend> getAllFriends(){
        return friends;
    }
    @Override
    public Friend getFriend(String nick){
        return friends.get(getIndexOfFriend(nick));
    }
    @Override
    public int getIndexOfFriend(String name){
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getNick().equals(name)){
                return i;
            }
        }
        return -1; //returns -1 if no match found
    }
    @Override
    public void changeFriendAttr(String user, String attribute, String newValue){
        if(getIndexOfFriend(user) != -1){
            switch(attribute) {
                case "Fullname" -> friends.get(getIndexOfFriend(user)).setName(newValue);
                case "Image" -> friends.get(getIndexOfFriend(user)).setImage(newValue);
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
    @Override
    public void setFriendlist(List<Friend> newList){
        friends = newList;
        Collections.sort(friends, new Sortbynick());
    }
}
