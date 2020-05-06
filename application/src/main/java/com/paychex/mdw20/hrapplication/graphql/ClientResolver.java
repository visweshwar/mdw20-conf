package com.paychex.mdw20.hrapplication.graphql;

import com.paychex.mdw20.hrapplication.entity.Client;
import com.paychex.mdw20.hrapplication.entity.Employee;
import com.paychex.mdw20.hrapplication.entity.repository.EmployeeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientResolver extends MongoEntityBase<Client> {

    @Autowired
    //    private EmployeeReactiveRepository employeeRepository;
    private EmployeeRepository employeeRepository;
    @Value(value = "${application.region}")
    private String REGION;

    public List<Employee> getEmployees(Client client) {
        return employeeRepository.getAllByClientIdAndRegion(client.getClientId(), REGION)/*.collectList().block()*/;
    }

}
