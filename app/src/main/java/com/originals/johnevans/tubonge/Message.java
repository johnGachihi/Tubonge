package com.originals.johnevans.tubonge;

/**
 * Created by John on 4/24/2017.
 */

public class Message {

    private String senderId;
    private String iconPath;
    private String message;
    private String imageUri;
    private String receiverId;

    public  Message() {}

    public Message(String senderId, String iconPath, String message, String imageUri, String receiverId) {
        this.senderId = senderId;
        this.iconPath = iconPath;
        this.message = message;
        this.imageUri = imageUri;
        this.receiverId = receiverId;
    }


    /*public Message(String senderId, String iconPath, String message, String receiverId) {
        this.senderId = senderId;
        this.iconPath = iconPath;
        this.message = message;
        this.receiverId = receiverId;

    }*/

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
