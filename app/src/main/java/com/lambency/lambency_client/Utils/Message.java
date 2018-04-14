package com.lambency.lambency_client.Utils;

import java.sql.Timestamp;

/**
 * Created by Evan on 3/31/2018.
 */

public class Message {

    public String messageText;
    public String sender;
    public String createdAt;

    public Message(){

    }

    public Message(String messageText, String sender, String createdAt)
    {
        this.messageText = messageText;
        this.sender = sender;
        this.createdAt = createdAt;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSender()
    {
        return sender;
    }
}
