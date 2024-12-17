package com.example.login;

public class SharedModel {
    private static SharedModel instance;
    private String currentUserEmail;
    private String groupChatId; // Store the group chat ID

    private SharedModel() {
    }

    public static SharedModel getInstance() {
        if (instance == null) {
            instance = new SharedModel();
        }
        return instance;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }
}

