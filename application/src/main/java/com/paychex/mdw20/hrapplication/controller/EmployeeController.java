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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "Employee", description = "Operations about employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping(value = "/employee/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<EmployeeModel> getEmployee(@PathVariable(value = "id") String id) {
		ModelMapper modelMapper = new ModelMapper();
		return new ResponseEntity<>(modelMapper.map(employeeService.getEmployeeById(id), EmployeeModel.class),
				HttpStatus.OK);
	}

	@PostMapping(value = "/employee", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EmployeeModel> createEmployee(@RequestBody EmployeeModel employee) {
		ModelMapper modelMapper = new ModelMapper();
		Employee svcRequest = modelMapper.map(employee, Employee.class);

		return new ResponseEntity<>(modelMapper.map(employeeService.createEmployee(svcRequest), EmployeeModel.class),
				HttpStatus.CREATED);
	}

	@PutMapping(value = "/employee/{id}", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<EmployeeModel> updateEmployee(@RequestBody EmployeeModel employee,
			@PathVariable(value = "id") String id) {
		ModelMapper modelMapper = new ModelMapper();
		Employee svcRequest = modelMapper.map(employee, Employee.class);
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
}
/*
    Author: Visweshwar Ganesh 
   created on 3/8/20 
*/
