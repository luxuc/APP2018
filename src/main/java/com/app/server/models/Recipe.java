package com.app.server.models;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    public String getId() {
        return recipeId;
    }

    public String getHeadline() { return headline;}

    public String getUserId() {
        return userId;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public double getRating() { return rating; }

    public int getTotalRating() {
        return totalRating;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    String recipeId=null;
    String headline;
    String userId;
    String author;
    ArrayList<String> steps;
    ArrayList<String> ingredients;
    double rating;
    int totalRating;
    int numOfRatings;
    ArrayList<String> comments;

    public Recipe(String headline,String userId, String author, ArrayList<String> steps, ArrayList<String> ingredients, double rating, int totalRating, int numOfRatings, ArrayList<String> comments) {
        this.headline = headline;
        this.userId = userId;
        this.author = author;
        this.steps = steps;
        this.ingredients = ingredients;
        this.rating = rating;
        this.totalRating = totalRating;
        this.numOfRatings = numOfRatings;
        this.comments = comments;

    }
    public void setId(String id) {
        this.recipeId = id;
    }
}
