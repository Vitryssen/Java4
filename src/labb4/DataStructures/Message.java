/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.DataStructures;

/**
 *
 * @author André
 */
public class Message {
    private Friend author;
    private String message;
    public Message(Friend user, String newMessage){
        this.author = user;
        this.message = newMessage;
    }
    public Friend getAuthor(){
        return author;
    }
    public String getMessage(){
        return message;
    }
}
