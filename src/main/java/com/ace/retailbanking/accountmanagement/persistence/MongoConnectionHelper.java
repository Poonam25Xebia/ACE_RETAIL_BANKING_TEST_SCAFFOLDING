package com.ace.retailbanking.accountmanagement.persistence;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * Placeholder access point for MongoDB (future notification templates / logs). Configured from
 * {@code MONGODB_URI}; the client connects lazily, so the service starts without MongoDB running.
 * No collections or documents are modelled yet.
 */
@Component
public class MongoConnectionHelper {

  private final MongoTemplate mongoTemplate;

  public MongoConnectionHelper(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public MongoTemplate mongoTemplate() {
    return mongoTemplate;
  }

  public MongoDatabase database() {
    return mongoTemplate.getDb();
  }
}
