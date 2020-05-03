/*
 *
 *  * # Copyright 2020 Paychex, Inc.
 *  * # Licensed pursuant to the terms of the Apache License, Version 2.0 (the "License");
 *  * # your use of the Work is subject to the terms and conditions of the License.
 *  * # You may obtain a copy of the License at
 *  * #
 *  * # http://www.apache.org/licenses/LICENSE-2.0
 *  * #
 *  * # Disclaimer of Warranty. Unless required by applicable law or agreed to in writing, Licensor
 *  * # provides the Work (and each Contributor provides its Contributions) on an "AS IS" BASIS,
 *  * # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including,
 *  * # without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT,
 *  * # MERCHANTABILITY, OR FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible
 *  * # for determining the appropriateness of using or redistributing the Work and assume
 *  * # any risks associated with your exercise of permissions under this License.
 *
 */

package com.paychex.mdw20.hrapplication.service.impl;

import com.paychex.mdw20.hrapplication.entity.Client;
import com.paychex.mdw20.hrapplication.entity.Employee;
import com.paychex.mdw20.hrapplication.entity.repository.EmployeeRepository;
import com.paychex.mdw20.hrapplication.service.ClientService;
import com.paychex.mdw20.hrapplication.service.EmployeeService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    Author: Visweshwar Ganesh 
   created on 3/8/20 
*/
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	@Autowired
	private ClientService clientService;

	@Override
	public Employee getEmployeeById(String id) {
		return employeeRepository.findByEmployeeId(id);
	}

	@Override
	public Employee createEmployee(Employee employee) {
		Client client = clientService.getClientById(employee.getClientId());
		employee.setEmployeeId(UUID.randomUUID().toString());
		employee.setPremium(client.isPremium());
		employee.setCountry(client.getCountry());
		return employeeRepository.insert(employee);
	}

	@Override
	public boolean updateEmployee(Employee employee, String id) {

		Employee employeeEntity = employeeRepository.findByEmployeeId(id);
		Employee finalEmployee = null;
		try {
			finalEmployee = (Employee) employeeEntity.clone();
		} catch (CloneNotSupportedException e) {
			logger.error(e.getMessage());
		}
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(employee, finalEmployee);

		return employeeRepository.doUpdate(employeeEntity, finalEmployee);

	}

	@Override
	public void deleteEmployee(String id) {
		employeeRepository.deleteByEmployeeId(id);
	}

	@Override
	public List<Employee> loadEmployees(List<Employee> employees) {

		List<String> clientIds = employees.stream().map(ee -> ee.getClientId()).collect(Collectors.toList());

		List<Client> clients = clientService.getClientsById(clientIds);
		Map<String, Client> clientMap = clients.stream().collect(
				Collectors.toMap(Client::getClientId, Function.identity()));
		employees.stream().map(ee -> {
			ee.setEmployeeId(UUID.randomUUID().toString());
			ee.setPremium(clientMap.get(ee.getClientId()).isPremium());
			ee.setCountry(clientMap.get(ee.getClientId()).getCountry());
			return ee;
		});

		return employeeRepository.saveAll(employees);
	}
}
