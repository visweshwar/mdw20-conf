package com.paychex.mdw20.hrapplication.graphql;

import com.paychex.mdw20.hrapplication.entity.Client;
import com.paychex.mdw20.hrapplication.entity.Employee;
import com.paychex.mdw20.hrapplication.entity.repository.ClientReactiveRepository;
import com.paychex.mdw20.hrapplication.entity.repository.EmployeeRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    @Autowired
    private ClientReactiveRepository clientRepository;

    @Value(value = "${application.region}")
    private String REGION;
    @Autowired
    //    private EmployeeReactiveRepository employeeRepository;
    private EmployeeRepository employeeRepository;

    Client clientById(String id) {
        return clientRepository.findByClientId(id).block();
    }

    Employee employeeById(String id) {
        return employeeRepository.findByEmployeeIdAndRegion(id, REGION)/*.block()*/;
    }
}
