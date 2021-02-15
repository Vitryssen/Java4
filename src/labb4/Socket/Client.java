/*
 * André Nordlund
 * 2021-02-12
 * Java 2
 * Lab 4
 */
package labb4.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import labb4.DataStructures.Friend;

/**
 *
 * @author André
 */
public class Client implements HostListener {

    private Socket hostSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String host;
    private final int port;

    private final String register = "<REGISTER><%1$s><%2$s><%3$s><IMAGE>";
    private final String privateMessage = "<PRIVATE><%1$s><%2$s><%3$s>";
    private final String publicMessage = "<PUBLIC><%1$s><%2$s>";
    private final String logout = "<LOGOUT><%1$s>";

    private String nickName;
    private String fullName;
    private boolean connected;
    private String ip;
    
    private List<String> messages = new ArrayList<>();
    private List<Friend> friends = new ArrayList<>();
    private List<String> publicMsg = new ArrayList<>();
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException uhe) {
            System.err.println("Coudn't access local interface adress, using 127.0.0.1");
            this.ip = "127.0.0.1";
        }
    }

    public void register(String nickname, String fullname) {
        assertConnected();
        this.nickName = nickname;
        this.fullName = fullname;
        sendRegister();

    }

    public void sendPublicMessage(String messageText) {
        String message = String.format(publicMessage, nickName, messageText);
        publicMsg.add(message);
        out.println(message);
        out.flush();
    }
    
    public void sendPrivateMessage(String messageText, String receiver) {
        String message = String.format(privateMessage, nickName,receiver, messageText);
        out.println(message);
        out.flush();
    }
    
    public void sendLogout() {
        String message = String.format(logout, nickName);
        out.println(message);
        out.flush();
        out.close();
        try {
            in.close();
            hostSocket.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    public void register(String name) {
        register(name, name);
    }

    public void connect() throws UnknownHostException, IOException {
        hostSocket = new Socket(host, port);
        out = new PrintWriter(hostSocket.getOutputStream());
        in = new BufferedReader(
                new InputStreamReader(hostSocket.getInputStream()));
        connected = true;
        new ListenerThread(this, in).start();
    }

    private void sendRegister() {
        assertConnected();
        String message = String.format(register, nickName, fullName, ip);
        out.println(message);
        out.flush();
    }

    private void assertConnected(){
        if (!connected) {
            System.out.println("Not connected");
        }
    }

    @Override
    public void messageRecieved(String message) {
        if(message.contains("<FRIEND>")){
            addFriend(message);
        }
        else if(message.contains("<LOGOUT>")){
            removeFriend(message);
        }
        else if(message.contains("<PUBLIC>")){
            System.out.println("Public msg "+message);
            publicMsg.add(message);
        }
        else if(message.contains("<PRIVATE>")){
            System.out.println("Private msg "+message);
        }
        messages.add(message);
    }
    public void addFriend(String newFriend){
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
    }
    public void removeFriend(String nick){
        String newNick = nick.substring(nick.indexOf('>')+1);
        newNick = newNick.substring(1, newNick.length()-1);
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getNick().equals(newNick))
                friends.remove(i);
        }
    }
    public List<String> getInputStream(){
        return messages;
    }
    public List<Friend> getFriends(){
        return friends;
    }
    public List<String> getPublicMessages(){
        return publicMsg;
    }
    private class ListenerThread extends Thread {

        private final HostListener listener;
        private final BufferedReader in;

        public ListenerThread(HostListener listener, BufferedReader in) {
            this.listener = listener;
            this.in = in;
        }

        public void run() {
            try {
                while (connected) {
                    String line = in.readLine();
                    listener.messageRecieved(line);
                }
            } catch (IOException ex) {
                System.err.println("Failed to write to socket");
            }
        }
    }

}
