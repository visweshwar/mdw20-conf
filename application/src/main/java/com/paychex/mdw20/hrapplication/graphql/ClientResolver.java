package com.paychex.mdw20.hrapplication.graphql;

import com.paychex.mdw20.hrapplication.model.ClientModel;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class ClientResolver implements GraphQLResolver<ClientModel> {
}
