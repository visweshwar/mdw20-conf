package com.paychex.mdw20.hrapplication.graphql;

import com.paychex.mdw20.hrapplication.model.ClientModel;
import com.paychex.mdw20.hrapplication.model.EmployeeModel;
import com.paychex.mdw20.hrapplication.service.ClientService;
import graphql.kickstart.tools.GraphQLResolver;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeResolver implements GraphQLResolver<EmployeeModel> {
    @Autowired
    private ClientService clientService;

    public ClientModel getClient(EmployeeModel e) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(clientService.getClientById(e.getClientId()), ClientModel.class);
    }
}
