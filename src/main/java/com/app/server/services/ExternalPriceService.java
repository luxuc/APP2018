package com.app.server.services;

import com.app.server.models.ExternalPrice;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ExternalPriceService {
    private static ExternalPriceService self;
    private ObjectWriter ow;
    private MongoCollection<Document> recipesCollection = null;
    private MongoCollection<Document> commentsCollection = null;

    private ExternalPriceService() {
        this.recipesCollection = MongoPool.getInstance().getCollection("recipes");
        this.commentsCollection = MongoPool.getInstance().getCollection("comments");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static ExternalPriceService getInstance(){
        if (self == null)
            self = new ExternalPriceService();
        return self;
    }

    public ExternalPrice getPrice(Object request) {

        ExternalPrice price = new ExternalPrice("beans", "1 pound", 20, "www.amazon.com");
        return  price;
    }
}
