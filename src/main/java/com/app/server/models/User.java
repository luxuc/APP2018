package com.app.server.models;

public class User {
    String userID = null;
    String username;
    String userScore;
    String preferedCuisine;
    String email;
    String password;

    public User(String userID) {
        this.userID = userID;
    }

    public User(String username, String userScore,
                String preferedCuisine, String email, String password) {
        this.username = username;
        this.userScore = userScore;
        this.preferedCuisine = preferedCuisine;
        this.email = email;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public String getPreferedCuisine() {
        return preferedCuisine;
    }

    public void setPreferedCuisine(String preferedCuisine) {
        this.preferedCuisine = preferedCuisine;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
