package com.app.server.services;

import com.app.server.http.utils.APPResponse;
import com.app.server.models.Notification;
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

public class NotificationService {

    private static NotificationService self;
    private ObjectWriter ow;
    private MongoCollection<Document> notificationsCollection = null;

    private NotificationService() {
        this.notificationsCollection = MongoPool.getInstance().getCollection("notifications");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static NotificationService getInstance(){
        if (self == null)
            self = new NotificationService();
        return self;
    }

    public Notification create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Notification notification = convertJsonToNotification(json);
            Document doc = convertNotificationToDocument(notification);
            notificationsCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            notification.setNotificationID(id.toString());
            return notification;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public ArrayList<Notification> getAll() {
        ArrayList<Notification> notificationList = new ArrayList<Notification>();

        FindIterable<Document> results = this.notificationsCollection.find();
        if (results == null) {
            return notificationList;
        }
        for (Document item : results) {
            Notification notification = convertDocumentToNotification(item);
            notificationList.add(notification);
        }
        return notificationList;
    }

    public Notification getOne(String id) {

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = notificationsCollection.find(query).first();
        if (item == null) {
            return  null;
        }
        return convertDocumentToNotification(item);
    }

    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        notificationsCollection.deleteOne(query);
        return new JSONObject();
    }

    public Object deleteAll() {
        notificationsCollection.deleteMany(new BasicDBObject());
        return new JSONObject();
    }

    public Object update(String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("userID"))
                doc.append("userID",json.getString("userID"));
            if (json.has("recipeID"))
                doc.append("recipeID",json.getString("recipeID"));
            if (json.has("time"))
                doc.append("time",json.getString("time"));
            if (json.has("content"))
                doc.append("content",json.getString("content"));

            Document set = new Document("$set", doc);
            notificationsCollection.updateOne(query,set);
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

    private Notification convertDocumentToNotification(Document item) {
        Notification notification = new Notification(
                item.getString("userID"),
                item.getString("recipeID"),
                item.getString("time"),
                item.getString("content")
        );
        notification.setNotificationID(item.getObjectId("_id").toString()); //not sure
        return notification;
    }

    private Notification convertJsonToNotification(JSONObject json){
        Notification notification = new Notification( json.getString("userID"),
                json.getString("recipeID"),
                json.getString("time"),
                json.getString("content")
        );
        return notification;
    }

    private Document convertNotificationToDocument(Notification notification){
        Document doc = new Document("userID", notification.getUserID())
                .append("recipeID", notification.getRecipeID())
                .append("time", notification.getTime())
                .append("", notification.getContent());
        return doc;
    }
}
