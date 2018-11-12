package com.app.client;

import com.app.server.models.Comment;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;

public class Client {
    public static void main(String[] argv) {
        Client client = new Client();

        doDeleteAllNotification();
        doDeleteAllUser();
        doUserPost("LeeSin", "100",
                "Korean","123@361.com","123456");
        doUserPost("Master Yi","100", "Chinese", "yi@361.com",
                "123456");
        doUserPost("Ryze Garen","0", "123 edison", "RGaren@361.com",
                "Mexican123456");
        doNotificationPost("001", "020", "2018-08-08", "This is test1");
        doNotificationPost("002", "020", "2018-08-08", "This is test2");
        doNotificationPost("003", "020", "2018-08-08", "This is test3");
        doGetAllNotification();
    }

    public static void doUserPost(String username, String userScore,
                                  String preferedCuisine, String email, String password){
        try {
            URL url = new URL("http://localhost:8080/api/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject notification = new JSONObject();
            notification.put("username", username);
            notification.put("userScore", userScore);
            notification.put("preferedCuisine", preferedCuisine);
            notification.put("email", email);
            notification.put("password", password);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(notification.toString());
            wr.flush();

            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            in.close();
            con.disconnect();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void doNotificationPost(String userID, String recipeID, String time, String content){
        try {
            URL url = new URL("http://localhost:8080/api/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject notification = new JSONObject();
            notification.put("userID", userID);
            notification.put("recipeID", recipeID);
            notification.put("time", time);
            notification.put("content", content);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(notification.toString());
            wr.flush();

            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            in.close();
            con.disconnect();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void doGetAllNotification() {
        try {
            URL url = new URL("http://localhost:8080/api/notifications");
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

    public static void doDeleteAllNotification() {
        try {
            URL url = new URL("http://localhost:8080/api/notifications");
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

    public static void doDeleteAllUser() {
        try {
            URL url = new URL("http://localhost:8080/api/users");
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
