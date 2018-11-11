package com.app.server.models;

import java.sql.Timestamp;

public class Notification {
    String notificationID;
    String userID;
    String recipeID;
    String time;
    String content;

    public Notification(String userID, String recipeID, String time, String content) {
        this.userID = userID;
        this.recipeID = recipeID;
        this.time = time;
        this.content = content;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
