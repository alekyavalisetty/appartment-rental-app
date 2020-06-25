package com.pramod.apartmentrental.ChatRoom;

public class ChatRoomObject {
    private Boolean isCurrentUser;
    private String chatMessage;


    public ChatRoomObject(Boolean isCurrentUser, String chatMessage) {
        this.isCurrentUser = isCurrentUser;
        this.chatMessage = chatMessage;
    }

    public Boolean getCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        isCurrentUser = currentUser;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}
