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
import com.paychex.mdw20.hrapplication.entity.repository.ClientRepository;
import com.paychex.mdw20.hrapplication.model.ClientModel;
import com.paychex.mdw20.hrapplication.service.ClientService;
import java.util.UUID;
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
public class ClientServiceImpl implements ClientService {

	Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

	@Autowired
	private ClientRepository clientRepository;

	@Override
	public ClientModel getClientById(String id) {
		Client clt = clientRepository.findByClientId(id);
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(clt, ClientModel.class);
	}

	@Override
	public ClientModel createClient(Client client) {
		client.setClientId(UUID.randomUUID().toString());
		Client clt = clientRepository.insert(client);
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(clt, ClientModel.class);

	}

	@Override
	public boolean updateClient(Client client, String id) {
		Client clientEntity = clientRepository.findByClientId(id);
		Client finalClient = null;
		try {
			finalClient = (Client) clientEntity.clone();
		} catch (CloneNotSupportedException e) {
			logger.error(e.getMessage());
		}
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(client, finalClient);

		return clientRepository.doUpdate(clientEntity, finalClient);
	}

	@Override
	public void deleteClient(String id) {
		clientRepository.deleteByClientId(id);
	}
}
