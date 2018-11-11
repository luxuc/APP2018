package com.app.server.services;

import com.app.server.models.Comment;
import com.app.server.models.Recipe;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipesService {

    private static RecipesService self;
    private ObjectWriter ow;
    private MongoCollection<Document> recipesCollection = null;
    private MongoCollection<Document> commentsCollection = null;

    private RecipesService() {
        this.recipesCollection = MongoPool.getInstance().getCollection("recipes");
        this.commentsCollection = MongoPool.getInstance().getCollection("comments");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static RecipesService getInstance(){
        if (self == null)
            self = new RecipesService();
        return self;
    }

    public ArrayList<Recipe> getAll() {
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

        FindIterable<Document> results = this.recipesCollection.find();
        if (results == null) {
            return recipeList;
        }
        for (Document item : results) {
            Recipe recipe = convertDocumentToRecipe(item);
            recipeList.add(recipe);
        }
        return recipeList;
    }

    public ArrayList<Comment> getAllComments(String rid) {
        ArrayList<Comment> commentList = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("recipeId", rid);

        FindIterable<Document> results = this.commentsCollection.find(query);
        if (results == null) {
            return commentList;
        }
        for (Document item : results) {
            Comment comment = convertDocumentToComment(item);
            commentList.add(comment);
        }
        return commentList;
    }

    public Recipe getOne(String id) {

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = recipesCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return  convertDocumentToRecipe(item);
    }

    public Comment getOneComment(String id, String cid) {

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(cid));

        Document item = commentsCollection.find(query).first();
        if (item == null) {
            return  null;
        }
        return  convertDocumentToComment(item);
    }

    public Recipe create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Recipe recipe = convertJsonToRecipe(json);
            Document doc = convertRecipeToDocument(recipe);
            recipesCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            recipe.setId(id.toString());
            return recipe;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public Comment createOneComment(String id, Object request) {
        try {
            // store comments
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Comment comment = convertJsonToComment(json);
            Integer rating = comment.getRating();
            Document doc = convertCommentToDocument(comment);
            commentsCollection.insertOne(doc);
            ObjectId cid = (ObjectId)doc.get( "_id" );
            comment.setId(cid.toString());

            // add comment id into recipe.comments
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));
            Document item = recipesCollection.find(query).first();
            Recipe recipe = convertDocumentToRecipe(item);
            ArrayList<String> comments = recipe.getComments();
            comments.add(cid.toString());
            Integer totalRating = recipe.getTotalRating();
            totalRating = totalRating + rating;
            Integer numOfRatings = recipe.getNumOfRatings();
            numOfRatings = numOfRatings + 1;
            Double newRating = totalRating / numOfRatings * 1.0;

            Document docRecipe = new Document();
            docRecipe.append("comments", comments);
            docRecipe.append("totalRating", totalRating);
            docRecipe.append("numOfRatings", numOfRatings);
            docRecipe.append("rating", newRating);
            Document set = new Document("$set", docRecipe);
            recipesCollection.updateOne(query,set);

            return comment;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public Object update(String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("headline"))
                doc.append("headline",json.getString("headline"));
            if (json.has("userId"))
                doc.append("userId",json.getString("userId"));
            if (json.has("author"))
                doc.append("author",json.getString("author"));
            if (json.has("steps"))
                doc.append("steps", json.getJSONArray("steps"));
            if (json.has("ingredients"))
                doc.append("ingredients", json.getJSONArray("ingredients"));
            if (json.has("rating"))
                doc.append("rating",json.getDouble("rating"));
            if (json.has("totalRating"))
                doc.append("totalRating",json.getInt("totalRating"));
            if (json.has("numOfRatings"))
                doc.append("numOfRatings",json.getInt("numOfRatings"));
            if (json.has("comments"))
                doc.append("comments",json.getJSONArray("comments"));

            Document set = new Document("$set", doc);
            recipesCollection.updateOne(query,set);
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

    public Object updateOneComment(String cid, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(cid));

            Document doc = new Document();
            if (json.has("recipeId"))
                doc.append("recipeId",json.getString("recipeId"));
            if (json.has("userId"))
                doc.append("userId",json.getString("userId"));
            if (json.has("rating"))
                doc.append("rating",json.getString("rating"));
            if (json.has("comment"))
                doc.append("comment",json.getString("comment"));
            if (json.has("time"))
                doc.append("time",json.getString("time"));

            Document set = new Document("$set", doc);
            commentsCollection.updateOne(query,set);
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

    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        recipesCollection.deleteOne(query);

        return new JSONObject();
    }

    public Object deleteOneComment(String rid, String cid) {
        // change recipe rating
        // delete one comment
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(cid));

        commentsCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        recipesCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    public Object deleteAllComments(String rid) {
        // change rating of recipe
        // delete comments
        BasicDBObject query = new BasicDBObject();
        query.put("recipeId", new ObjectId(rid));

        commentsCollection.deleteMany(query);

        return new JSONObject();
    }

    private Recipe convertDocumentToRecipe(Document item) {

        Recipe recipe = new Recipe(
                item.getString("headline"),
                item.getString("userId"),
                item.getString("author"),
                (ArrayList<String>) item.get("steps"),
                (ArrayList<String>) item.get("ingredients"),
                item.getDouble("rating"),
                item.getInteger("totalRating"),
                item.getInteger("numOfRatings"),
                (ArrayList<String>) item.get("comments")
        );
        recipe.setId(item.getObjectId("_id").toString());
        return recipe;
    }

    private Comment convertDocumentToComment(Document item) {
        Comment comment = new Comment(
                item.getString("recipeId"),
                item.getString("userId"),
                item.getInteger("rating"),
                item.getString("comment"),
                item.getString("time")
        );
        comment.setId(item.getObjectId("_id").toString());
        return comment;
    }

    private Recipe convertJsonToRecipe(JSONObject json) {
        JSONArray jArray = json.getJSONArray("steps");
        ArrayList<String> stepsList = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                stepsList.add(jArray.getString(i));
            }
        }

        JSONArray ingredientsjArray = json.getJSONArray("ingredients");
        ArrayList<String> ingredientsList = new ArrayList<>();
        if (ingredientsjArray != null) {
            for (int i=0;i<ingredientsjArray.length();i++){
                ingredientsList.add(ingredientsjArray.getString(i));
            }
        }

        JSONArray commentsjArray = json.getJSONArray("comments");
        ArrayList<String> commentsList = new ArrayList<>();
        if (commentsjArray != null) {
            for (int i=0;i<commentsjArray.length();i++){
                commentsList.add(commentsjArray.getString(i));
            }
        }

        Recipe recipe = new Recipe(
                json.getString("headline"),
                json.getString("userId"),
                json.getString("author"),
                stepsList,
                ingredientsList,
                json.getDouble("rating"),
                json.getInt("totalRating"),
                json.getInt("numOfRatings"),
                commentsList
        );
        return recipe;

    }

    private Comment convertJsonToComment(JSONObject json){
        Comment comment = new Comment( json.getString("recipeId"),
                json.getString("userId"),
                json.getInt("rating"),
                json.getString("comment"),
                json.getString("time"));
        return comment;
    }

    private Document convertRecipeToDocument(Recipe recipe) {
        Document doc = new Document("headline", recipe.getHeadline())
                .append("userId", recipe.getUserId())
                .append("author", recipe.getAuthor())
                .append("steps", recipe.getSteps())
                .append("ingredients", recipe.getIngredients())
                .append("rating", recipe.getRating())
                .append("totalRating", recipe.getTotalRating())
                .append("numOfRatings", recipe.getNumOfRatings())
                .append("comments", recipe.getComments());
        return doc;

    }

    private Document convertCommentToDocument(Comment comment){
        Document doc = new Document("recipeId", comment.getRecipeId())
                .append("userId", comment.getUserId())
                .append("rating", comment.getRating())
                .append("comment", comment.getComment());
        return doc;
    }
}
