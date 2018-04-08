package com.lambency.lambency_client.Utils;

/**
 * Created by Evan on 3/31/2018.
 */

public class Message {

    public String messageText;
    public String sender;
    public String createdAt = "";

    public Message(){

    }

    public Message(String messageText, String sender)
    {
        this.messageText = messageText;
        this.sender = sender;
    }

    public String getMessage()
    {
        return messageText;
    }

    public String getSender()
    {
        return sender;
    }
}
