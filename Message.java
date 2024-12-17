package com.example.login;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public Message(int id, String sender, String content, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() { return id; }
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
}