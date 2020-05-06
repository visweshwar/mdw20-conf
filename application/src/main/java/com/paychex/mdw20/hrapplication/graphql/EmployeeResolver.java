package com.paychex.mdw20.hrapplication.graphql;

import com.paychex.mdw20.hrapplication.entity.Client;
import com.paychex.mdw20.hrapplication.entity.Employee;
import com.paychex.mdw20.hrapplication.entity.repository.ClientReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeResolver extends MongoEntityBase<Employee> {
    @Autowired
    private ClientReactiveRepository clientRepository;

    public Client getClient(Employee e) {
        return clientRepository.findByClientId(e.getClientId()).block();
    }
}
