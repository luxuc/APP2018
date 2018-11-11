package com.app.client;

import com.app.server.models.Comment;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Client {
    public static void main(String[] argv) {
//        doDeleteAll("http://localhost:8080/api/recipes");
//        ArrayList<String> comments = new ArrayList<>();
//        ArrayList<String> steps = new ArrayList<>();
//        steps.add("Boil the water");
//        steps.add("add ingredients");
//        ArrayList<String> ingredients = new ArrayList<>();
//        ingredients.add("water");
//        ingredients.add("potato");
//        doPostRecipe("recipe one", "123", "Jay Young", steps, ingredients, 3.5, 7, 2, comments);
//        ArrayList<String> steps2 = new ArrayList<>();
//        steps2.add("Put all ingredients at once");
//        steps2.add("Boil for ten hours");
//        ArrayList<String> ingredients2 = new ArrayList<>();
//        ingredients2.add("Blue Fin Tuna");
//        ingredients2.add("Kobe Beef");
//        ingredients2.add("Free range chicken Thigh");
//        ingredients2.add("Secret Soup Base");
//        doPostRecipe("big feast", "0593", "CHEF Goon", steps2, ingredients2, 5.0, 5, 1, comments);
//        doGetAll("http://localhost:8080/api/recipes");

        doDeleteAll("http://localhost:8080/api/recipes/5bc6d2a159035cad2f645c29/comments");
        doPostComment("5bc6d2a159035cad2f645c29", "userIdPlaceHolder", 5, "Nice Recipe! Will try it!", "1:49 11/17");
        doPostComment("5bc6d2a159035cad2f645c29", "userIdPlaceHolder123", 5, "Looks Good!", "1:49 11/17");
        doGetAll("http://localhost:8080/api/recipes/5bc6d2a159035cad2f645c29/comments");
    }



    public static void doPost(String firstName,String middleName, String lastName, String address1, String address2,
                              String city, String state, String country, String postalCode) {
        try {
            URL url = new URL("http://localhost:8080/api/drivers");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject driver = new JSONObject();
            driver.put("firstName",firstName);
            driver.put("middleName",middleName);
            driver.put("lastName",lastName);
            driver.put("address1",address1);
            driver.put("address2",address2);
            driver.put("city",city);
            driver.put("state",state);
            driver.put("country",country);
            driver.put("postalCode",postalCode);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(driver.toString());
            wr.flush();


            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);
            con.disconnect();
        }
        catch(Exception e) {
            e.printStackTrace();

        }

    }

    public static void doPostRecipe(String headline, String userId, String author, List<String> steps, List<String> ingredients, double rating, int totalRating, int numOfRatings, List<String> comments){
        try {
            URL url = new URL("http://localhost:8080/api/recipes");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject recipe = new JSONObject();
            recipe.put("headline", headline);
            recipe.put("userId", userId);
            recipe.put("author", author);
            recipe.put("steps", steps);
            recipe.put("ingredients", ingredients);
            recipe.put("rating", rating);
            recipe.put("totalRating", totalRating);
            recipe.put("numOfRatings",numOfRatings);
            recipe.put("comments", comments);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(recipe.toString());
            wr.flush();


            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);
            con.disconnect();
        }
        catch(Exception e) {
            e.printStackTrace();

        }

    }

    public static void doPostComment(String recipeId, String userId, Integer rating, String comment, String time){
        try {
            URL url = new URL("http://localhost:8080/api/recipes/" + recipeId + "/comments");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject oneComment = new JSONObject();
            oneComment.put("recipeId", recipeId);
            oneComment.put("userId", userId);
            oneComment.put("rating", rating);
            oneComment.put("comment", comment);
            oneComment.put("time", time);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(oneComment.toString());
            wr.flush();

            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);
            con.disconnect();
        }
        catch(Exception e) {
            e.printStackTrace();

        }

    }


    public static void doGetAll(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void doDeleteAll(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }






}
