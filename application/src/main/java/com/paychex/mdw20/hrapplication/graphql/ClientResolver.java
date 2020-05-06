package com.paychex.mdw20.hrapplication.graphql;

import com.paychex.mdw20.hrapplication.entity.Client;
import com.paychex.mdw20.hrapplication.entity.Employee;
import com.paychex.mdw20.hrapplication.entity.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientResolver extends MongoEntityBase<Client> {

    @Autowired
//    private EmployeeReactiveRepository employeeRepository;
    private EmployeeRepository employeeRepository;

    public List<Employee> getEmployees(Client client) {
        return employeeRepository.getAllByClientId(client.getClientId())/*.collectList().block()*/;
    }

}
