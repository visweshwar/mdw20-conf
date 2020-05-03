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

import com.paychex.mdw20.hrapplication.entity.Client;
import com.paychex.mdw20.hrapplication.model.ClientModel;
import com.paychex.mdw20.hrapplication.service.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    Author: Visweshwar Ganesh 
   created on 3/8/20 
*/
@RestController
@RequestMapping(value = "/api")
@Tag(name = "Client", description = "Operations about client")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@GetMapping(value = "/client/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ClientModel> getClient(@PathVariable(value = "id") String id) {
		ModelMapper modelMapper = new ModelMapper();
		return new ResponseEntity<>(modelMapper.map(clientService.getClientById(id), ClientModel.class), HttpStatus.OK);
	}

	@PostMapping(value = "/client", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ClientModel> createClient(@RequestBody Client client) {
		ModelMapper modelMapper = new ModelMapper();
		return new ResponseEntity<>(modelMapper.map(clientService.createClient(client), ClientModel.class),
				HttpStatus.CREATED);
	}

	@PutMapping(value = "/client/{id}", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ClientModel> updateClient(@RequestBody Client client, @PathVariable(value = "id") String id) {
		ModelMapper modelMapper = new ModelMapper();
		if (clientService.updateClient(client, id)) {
			return new ResponseEntity<>(modelMapper.map(clientService.getClientById(id), ClientModel.class),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(modelMapper.map(clientService.getClientById(id), ClientModel.class),
					HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/client/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteClient(@PathVariable(value = "id") String id) {
		clientService.deleteClient(id);
	}

	@PostMapping(value = "/client/{id}/migrate/{status}")
	@ResponseStatus(HttpStatus.OK)
	public boolean migrateClient(@PathVariable(value = "id") String id,
			@PathVariable(value = "status") boolean premiumStatus) {
		return clientService.migrateClient(id, premiumStatus);
	}

	@GetMapping(value = "/client/{name}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ClientModel> getClientByName(@PathVariable(value = "name") String name) {
		ModelMapper modelMapper = new ModelMapper();
		return new ResponseEntity<>(modelMapper.map(clientService.getClientByName(name), ClientModel.class),
				HttpStatus.OK);
	}

	@PostMapping(value = "/client/")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ClientModel> getClientByIds(@PathVariable(value = "clientIds") List<String> clientIds) {
		ModelMapper modelMapper = new ModelMapper();
		return new ResponseEntity<>(modelMapper.map(clientService.getClientsById(clientIds), ClientModel.class),
				HttpStatus.OK);
	}
}
