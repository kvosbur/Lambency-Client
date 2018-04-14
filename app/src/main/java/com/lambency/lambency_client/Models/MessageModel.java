package com.lambency.lambency_client.Models;

import java.sql.Timestamp;

/**
 * Created by Evan on 3/31/2018.
 */

public class MessageModel {

    public String messageText;
    public String sender;
    public String createdAt;

    public MessageModel(){

    }

    public MessageModel(String messageText, String sender, String createdAt)
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
