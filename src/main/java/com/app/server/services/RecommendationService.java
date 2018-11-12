package com.app.server.services;

import com.app.server.models.Recommendation;
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

public class RecommendationService {
    private static RecommendationService self;
    private ObjectWriter ow;
    private MongoCollection<Document> recommendationCollection = null;

    private RecommendationService(){
        this.recommendationCollection = MongoPool.getInstance().getCollection("recommendations");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static RecommendationService getInstance(){
        if (self == null)
            self = new RecommendationService();
        return self;
    }

    public Recommendation create(Object request){
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Recommendation recommendation = convertJsonToRecommendation(json);
            Document doc = convertAdminToDocument(recommendation);
            recommendationCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            recommendation.setRecommendationID(id.toString());
            return recommendation;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public ArrayList<Recommendation> getAll() {
        ArrayList<Recommendation> recommendationlist = new ArrayList<Recommendation>();

        FindIterable<Document> results = this.recommendationCollection.find();
        if (results == null) {
            return recommendationlist;
        }
        for (Document item : results) {
            Recommendation recommendation = convertDocumentToRecommendation(item);
            recommendationlist.add(recommendation);
        }
        return recommendationlist;
    }

    public Recommendation getOne(String id) {

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = recommendationCollection.find(query).first();
        if (item == null) {
            return  null;
        }
        return convertDocumentToRecommendation(item);
    }

    private Recommendation convertJsonToRecommendation(JSONObject json){
        Recommendation recommendation = new Recommendation( json.getString("userID"),
                json.getString("recipeID"),
                json.getString("time")
        );
        return recommendation;
    }

    private Document convertAdminToDocument(Recommendation recommendation){
        Document doc = new Document("userID", recommendation.getUserID())
                .append("recipeID", recommendation.getRecipeID())
                .append("time", recommendation.getTime());
        return doc;
    }

    private Recommendation convertDocumentToRecommendation(Document item) {
        Recommendation recommendation = new Recommendation(
                item.getString("userID"),
                item.getString("recipeID"),
                item.getString("time")
        );
        recommendation.setRecommendationID(item.getObjectId("_id").toString());
        return recommendation;
    }

    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        recommendationCollection.deleteOne(query);
        return new JSONObject();
    }

    public Object deleteAll() {
        recommendationCollection.deleteMany(new BasicDBObject());
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

            Document set = new Document("$set", doc);
            recommendationCollection.updateOne(query,set);
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
}
