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

package com.paychex.mdw20.hrapplication.controller;

import com.paychex.mdw20.hrapplication.entity.Employee;
import com.paychex.mdw20.hrapplication.model.EmployeeModel;
import com.paychex.mdw20.hrapplication.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "Employee", description = "Operations about employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	@Value(value = "${application.region}")
	private String REGION;

	@GetMapping(value = "/employee/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<EmployeeModel> getEmployee(@PathVariable(value = "id") String id) {
		ModelMapper modelMapper = new ModelMapper();
		Employee response = employeeService.getEmployeeById(id);
		if (response == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(modelMapper.map(response, EmployeeModel.class), HttpStatus.OK);
	}

	@PostMapping(value = "/employee", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EmployeeModel> createEmployee(@RequestBody EmployeeModel employee) {
		ModelMapper modelMapper = new ModelMapper();
		Employee svcRequest = modelMapper.typeMap(EmployeeModel.class, Employee.class).addMapping(mapper -> REGION,
				Employee::setRegion).map(employee);

		return new ResponseEntity<>(modelMapper.map(employeeService.createEmployee(svcRequest), EmployeeModel.class),
				HttpStatus.CREATED);
	}

	@PutMapping(value = "/employee/{id}", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<EmployeeModel> updateEmployee(@RequestBody EmployeeModel employee,
			@PathVariable(value = "id") String id) {
		ModelMapper modelMapper = new ModelMapper();
		Employee svcRequest = modelMapper.typeMap(EmployeeModel.class, Employee.class).addMapping(mapper -> REGION,
				Employee::setRegion).map(employee);
		if (employeeService.updateEmployee(svcRequest, id)) {
			return new ResponseEntity<>(modelMapper.map(employeeService.getEmployeeById(id), EmployeeModel.class),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(modelMapper.map(employeeService.getEmployeeById(id), EmployeeModel.class),
					HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/employee/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteEmployee(@PathVariable(value = "id") String id) {
		employeeService.deleteEmployee(id);
	}

	@PostMapping(value = "/employees", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity createEmployee(@RequestBody List<EmployeeModel> employee) {
		ModelMapper modelMapper = new ModelMapper();
		List<Employee> svcRequest = employee.stream().map(
				ee -> modelMapper.typeMap(EmployeeModel.class, Employee.class).addMapping(mapper -> REGION,
						Employee::setRegion).map(ee)).collect(Collectors.toList());

		List<Employee> svcResponse = employeeService.loadEmployees(svcRequest);

		List<EmployeeModel> response = svcResponse.stream().map(
				eem -> modelMapper.map(eem, EmployeeModel.class)).collect(Collectors.toList());

		return new ResponseEntity(response, HttpStatus.CREATED);
	}

}
