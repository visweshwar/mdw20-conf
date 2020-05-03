package com.paychex.mdw20.hrapplication.graphql;

import com.paychex.mdw20.hrapplication.model.ClientModel;
import com.paychex.mdw20.hrapplication.model.EmployeeModel;
import com.paychex.mdw20.hrapplication.service.ClientService;
import com.paychex.mdw20.hrapplication.service.EmployeeService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmployeeService employeeService;

    ClientModel clientById(String id) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(clientService.getClientById(id), ClientModel.class);
    }

    EmployeeModel employeeById(String id) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(employeeService.getEmployeeById(id), EmployeeModel.class);
    }
}
