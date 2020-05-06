package com.paychex.mdw20.hrapplication.graphql;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.paychex.mdw20.hrapplication.entity.CustomMongoEntity;
import graphql.kickstart.tools.GraphQLResolver;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;

public abstract class MongoEntityBase<T extends CustomMongoEntity>
    implements GraphQLResolver<T> {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public String getReplicaSetName(T entity) {
        return Mono.from( mongoTemplate
                .getCollection(entity.getCollectionName())
                .find(entity.getShardQuery().getQueryObject())
                .modifiers(new Document("$explain", true))
                .first()
        ).map(doc -> {
            DocumentContext json = JsonPath.parse(doc.toJson());
            String stage = json.read("$['queryPlanner']['winningPlan']['stage']", String.class);
            return "SINGLE_SHARD".equals(stage)
                    ? json.read("$['queryPlanner']['winningPlan']['shards'][0]['shardName']", String.class)
                    : "Unknown"
                    ;
        }).block();
    }
}
