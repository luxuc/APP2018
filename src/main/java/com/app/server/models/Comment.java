package com.app.server.models;

import java.util.Date;

public class Comment {

    public String getId() {
        return commentId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getUserId() {
        return userId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() { return comment; }

    public String getTime() { return time; }

    String commentId = null;
    String recipeId;
    String userId;
    int rating;
    String comment;
    String time;

    public Comment(String recipeId, String userId, int rating, String comment, String time) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.time = time;
    }

    public void setId(String id) {
        this.commentId = id;
    }
}