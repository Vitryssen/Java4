 Threads and Networking
Overview

 

This objective of this lab is to enable the communication aspect of the chat program. This covers material in both the 9th and 10th Lectures. Note that this is a single lab assignment covering both lab sessions.
Requirements

Extend the current assignment to make it capable of sending messages to the main server and participate in the chat room application. We achieve this by using a simple protocol to exchange messages with the server, the protocol is outlined below:

 
Messages Sent to the Server by The Client (Your Program)

Registering

In order to use the chatroom, each new user must submit a login request. The server expects a login request of the format:

<REGISTER><NICKNAME><FULLNAME><IP_ADDRESS><IMAGE> 

This registers the user with the details provided.

Nickname above, refers to the nickname of the person wishing to regisrter and use the chatroom. During this lab session this might be your nickname. Others will see you by this nickname.

On successful registration the chat server will return a welcome message.

Sending a Public Message

When the user wishes to send a public message to the entire chatroom, it is required to be of the format:

<PUBLIC><YOURNICKNAME><Message> 

 Here, nickname is the nickname of the person using your chat client(yourself) and the message is the message you wish to send to the entire chatroom.

 Sending a Private Message

A PRIVATE message is sent of the format:

<PRIVATE><YOURNICKNAME><FRIENDSNICKNAME><Message>

This message is forwarded to the person specified in <FRIENDSNICKNAME>  as a private message.

 Logging Out

On exiting the chatroom, a logout command must be sent of the following format:

<LOGOUT><YOURNICKNAME>

This will notfiy all the users in the room that you have exited. You will be removed from the current online friend's list as well.

 
Messages Received by the Client  (Sent from the Server)

In addition to sending messages, your program must listen for, and handle messages from the server as well, these include:

Friend Messages:

These are used to notify the client of a new friend in the chatroom that should be added to the friends list.

Notification of a new FRIEND is sent in the format:

<FRIEND><YOURNICKNAME><FULLNAME><IPADDRESS><IMAGE> 

Logout Messages:

These notify the client when someone has logged out from the chatroom.

LOGOUT  notifications are of the format

 <LOGOUT><YOURNICKNAME>

Public Messages:

This informs the client when a message has been sent to the entire chatroom and are of the format: 

PUBLIC message notification are of the format:

<PUBLIC><NICKNAME><Message>

 Private Messages:

This informs the client when a private message has been sent to this user only. 

  PRIVATE Message notifications are  of the format:

<PRIVATE><NICKNAME><Message>

 
Instructions:

Extend the current program to enable multi-threading and networking.  All server communication is done using TCP/IP sockets.

 

The implementation is expected to be done is such a way that a user can send and receive multiple messages at the same time, use the GUI for scrolling, typing, etc while still receiving message (multiple threads). Be able to have  multiple private conversations at the same time. The friends list being viewed should be current, that is to say only showing the users currently logged in. Messages of the wrong format will be ignored by the server!
