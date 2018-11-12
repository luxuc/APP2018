package com.app.server.services;

import com.app.server.models.Admin;
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

public class AdminService {
    private static AdminService self;
    private ObjectWriter ow;
    private MongoCollection<Document> adminCollection = null;

    private AdminService(){
        this.adminCollection = MongoPool.getInstance().getCollection("admins");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static AdminService getInstance(){
        if (self == null)
            self = new AdminService();
        return self;
    }

    public Admin create(Object request){
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Admin admin = convertJsonToAdmin(json);
            Document doc = convertAdminToDocument(admin);
            adminCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            admin.setAdminID(id.toString());
            return admin;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public ArrayList<Admin> getAll() {
        ArrayList<Admin> adminlist = new ArrayList<Admin>();

        FindIterable<Document> results = this.adminCollection.find();
        if (results == null) {
            return adminlist;
        }
        for (Document item : results) {
            Admin admin = convertDocumentToAdmin(item);
            adminlist.add(admin);
        }
        return adminlist;
    }

    public Admin getOne(String id) {

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = adminCollection.find(query).first();
        if (item == null) {
            return  null;
        }
        return convertDocumentToAdmin(item);
    }

    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        adminCollection.deleteOne(query);
        return new JSONObject();
    }

    public Object deleteAll() {
        adminCollection.deleteMany(new BasicDBObject());
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
            if (json.has("password"))
                doc.append("password",json.getString("password"));

            Document set = new Document("$set", doc);
            adminCollection.updateOne(query,set);
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

    private Admin convertJsonToAdmin(JSONObject json){
        Admin admin = new Admin( json.getString("username"),
                json.getString("password")
        );
        return admin;
    }

    private Document convertAdminToDocument(Admin admin){
        Document doc = new Document("username", admin.getUsername())
                .append("password", admin.getPassword());
        return doc;
    }

    private Admin convertDocumentToAdmin(Document item) {
        Admin admin = new Admin(
                item.getString("username"),
                item.getString("password")
        );
        admin.setAdminID(item.getObjectId("_id").toString());
        return admin;
    }
}
