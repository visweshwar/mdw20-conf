package com.paychex.mdw20.hrapplication.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paychex.mdw20.hrapplication.entity.Client;
import graphql.kickstart.tools.GraphQLResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.stereotype.Component;

@Component
public class ClientChangeEventResolver
        implements GraphQLResolver<ClientChangeEventResolver.EventWrapper> {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    private static final Logger LOGGER = LoggerFactory.getLogger(ClientChangeEventResolver.class);

    public String getOperationType(EventWrapper wrapper) {
        return wrapper.getEvent().getOperationType().getValue();
    }

    public Client getDocumentKey(EventWrapper wrapper) {
        Client key = null;
        String keyData = wrapper.getEvent().getRaw().getDocumentKey().toJson();
        try {
            key=JSON_MAPPER.readValue(keyData, Client.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return key;
    }

    public Client getFullDocument(EventWrapper wrapper) {
        return wrapper.getEvent().getBody();
    }

    // GraphQL Tools doesn't resolve correctly with generics, so we have to create a wrapper
    // class around the generic ChangeStreamEvent class to get this to work
    public static class EventWrapper {
        private ChangeStreamEvent<Client> event;
        public EventWrapper(ChangeStreamEvent<Client> event) {
            this.event = event;
        }
        public ChangeStreamEvent<Client> getEvent() {
            return event;
        }
    }

}
