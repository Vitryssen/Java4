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
    private final String publicMessage = "<PUBLIC><%1$s><%2$s>";
    private final String logout = "<LOGOUT><%1$s>";

    private String nickName;
    private String fullName;
    private boolean connected;
    private String ip;
    
    private List<String> messages = new ArrayList<>();
    private List<String> friends = new ArrayList<>();

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
            friends.add(message);
        }
        //System.out.println(message);
        messages.add(message);
    }
    
    public List<String> getInputStream(){
        return messages;
    }
    public List<String> getFriends(){
        return friends;
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
