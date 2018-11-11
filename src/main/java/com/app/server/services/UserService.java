package com.app.server.services;

import com.app.server.http.utils.APPResponse;
import com.app.server.models.Notification;
import com.app.server.models.User;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserService {
    private static UserService self;
    private ObjectWriter ow;
    private MongoCollection<Document> usersCollection = null;
    private MongoCollection<Document> notificationsCollection = null;

    private UserService(){
        this.usersCollection = MongoPool.getInstance().getCollection("users");
        this.notificationsCollection = MongoPool.getInstance().getCollection("notifications");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static UserService getInstance(){
        if (self == null)
            self = new UserService();
        return self;
    }

    public User create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            User user = convertJsonToNotification(json);
            Document doc = convertNotificationToDocument(user);
            usersCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            user.setUserID(id.toString());
            return user;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public ArrayList<User> getAll() {
        ArrayList<User> userList = new ArrayList<User>();

        FindIterable<Document> results = this.usersCollection.find();
        if (results == null) {
            return userList;
        }
        for (Document item : results) {
            User user = convertDocumentToUser(item);
            userList.add(user);
        }
        return userList;
    }

    public User getOne(String id) {

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = usersCollection.find(query).first();
        if (item == null) {
            return  null;
        }
        return convertDocumentToUser(item);
    }

    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        usersCollection.deleteOne(query);
        return new JSONObject();
    }

    public Object deleteAll() {
        usersCollection.deleteMany(new BasicDBObject());
        return new JSONObject();
    }

    public Object update(String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("username"))
                doc.append("username",json.getString("username"));
            if (json.has("userScore"))
                doc.append("userScore",json.getString("userScore"));
            if (json.has("preferedCuisine"))
                doc.append("preferedCuisine",json.getString("preferedCuisine"));
            if (json.has("email"))
                doc.append("email",json.getString("email"));
            if (json.has("password"))
                doc.append("password",json.getString("password"));

            Document set = new Document("$set", doc);
            usersCollection.updateOne(query,set);
            return request;

        } catch(JSONException e) {
            System.out.println("Failed to update a document");
            return null;


        }
        catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    private User convertDocumentToUser(Document item) {
        User notification = new User(
                item.getString("username"),
                item.getString("userScore"),
                item.getString("preferedCuisine"),
                item.getString("email"),
                item.getString("password")
        );
        notification.setUserID(item.getObjectId("_id").toString()); //not sure
        return notification;
    }

    private User convertJsonToNotification(JSONObject json){
        User user = new User( json.getString("username"),
                "0",
                json.getString("preferedCuisine"),
                json.getString("email"),
                json.getString("password")
        );
        return user;
    }

    private Document convertNotificationToDocument(User user){
        Document doc = new Document("username", user.getUsername())
                .append("userScore", user.getUserScore())
                .append("preferedCuisine", user.getPreferedCuisine())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        return doc;
    }

    public Notification createNotificationForUser(String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Notification notification = convertJsonToNotification(json, id);
            Document doc = convertNotificationToDocument(notification);
            notificationsCollection.insertOne(doc);
            ObjectId notificationId = (ObjectId)doc.get( "_id" );
            notification.setNotificationID(notificationId.toString());
            return notification;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    private Notification convertJsonToNotification(JSONObject json, String id){
        Notification notification = new Notification( id,
                json.getString("recipeID"),
                json.getString("time"),
                json.getString("content"));
        return notification;
    }

    private Document convertNotificationToDocument(Notification notification){
        Document doc = new Document("userID", notification.getUserID())
                .append("recipeID", notification.getNotificationID())
                .append("time", notification.getTime())
                .append("content", notification.getContent());
        return doc;
    }
}
