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
public class Friend {
    private String nickname;
    private String fullname;
    private String ip;
    private String image;
    private String tag;
    public Friend(String newNick, String newName, String newIp, String newImage, String newTag){
        this.nickname = newNick;
        this.fullname = newName;
        this.ip = newIp;
        this.image = newImage;
        this.tag = newTag;
    }
    public Friend(){
        this.nickname = "";
        this.fullname = "";
        this.ip = "";
        this.image = "";
        this.tag = "";
    }
    //Setters
    public void setNick(String newNick){
        this.nickname = newNick;
    }
    public void setName(String newName){
        this.fullname = newName;
    }
    public void setIp(String newIp){
        this.ip = newIp;
    }
    public void setImage(String newImage){
        this.image = newImage;
    }
    public void setTag(String newTag){
        this.tag = newTag;
    }
    //Getters
    public String getNick(){
        return this.nickname;
    }
    public String getName(){
        return this.fullname;
    }
    public String getIp(){
        return this.ip;
    }
    public String getImage(){
        return this.image;
    }
    public String getTag(){
        return this.tag;
    }
    public void print(){
        System.out.println("Nick: "+this.nickname+"\nName: "+this.fullname+"\nIP: "+this.ip+"\nImage: "+this.image);
    }
}
