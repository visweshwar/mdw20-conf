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
import com.paychex.mdw20.hrapplication.service.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
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
	public ResponseEntity<Client> getClient(@PathVariable(value = "id") String id) {
		return new ResponseEntity<Client>(clientService.getClientById(id), HttpStatus.OK);
	}

	@PostMapping(value = "/client", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Client> createClient(@RequestBody Client client) {
		return new ResponseEntity<>(clientService.createClient(client), HttpStatus.CREATED);
	}

	@PutMapping(value = "/client/{id}", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Client> updateClient(@RequestBody Client client, @PathVariable(value = "id") UUID id) {
		return new ResponseEntity<>(clientService.updateClient(client, id), HttpStatus.OK);
	}

	@DeleteMapping(value = "/client/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteClient(@PathVariable(value = "id") String id) {
		clientService.deleteClient(id);
	}
}
