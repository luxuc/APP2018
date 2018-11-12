package com.app.server.models;

public class Recommendation {
    String RecommendationID = null;
    String userID;
    String recipeID;
    String time;

    public Recommendation(String recommendationID) {
        RecommendationID = recommendationID;
    }

    public Recommendation(String userID, String recipeID, String time) {
        this.userID = userID;
        this.recipeID = recipeID;
        this.time = time;
    }

    public String getRecommendationID() {
        return RecommendationID;
    }

    public void setRecommendationID(String recommendationID) {
        RecommendationID = recommendationID;
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
}
