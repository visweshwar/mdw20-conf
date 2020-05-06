package com.paychex.mdw20.hrapplication.graphql;

import com.paychex.mdw20.hrapplication.entity.Client;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class Subscription implements GraphQLSubscriptionResolver {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    Publisher<ClientChangeEventResolver.EventWrapper> clientChanges() {
        return mongoTemplate.changeStream(
                Client.COLLECTION_NAME,
                ChangeStreamOptions.builder()
                    .build(),
                Client.class
        ).log().map(e -> new ClientChangeEventResolver.EventWrapper(e));
    }
}