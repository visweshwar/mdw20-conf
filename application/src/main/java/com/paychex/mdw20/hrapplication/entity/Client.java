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

package com.paychex.mdw20.hrapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/*
    Author: Visweshwar Ganesh 
   created on 3/8/20 
*/
@Document(collection = Client.COLLECTION_NAME)
public class Client implements Cloneable, CustomMongoEntity {

	public static final String COLLECTION_NAME = "clientDetails";
	private String clientId;
	private String clientName;
	private boolean premium;
	private String address;
	private Countries country;
	@Id
	private ObjectId _id;
	private boolean active;

	public Client(String clientName) {
		this.clientName = clientName;
	}

	public Client() {
	}

	public Object get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Countries getCountry() {
		return country;
	}

	public void setCountry(Countries country) {
		this.country = country;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	@JsonIgnore
	public Map<String, Object> getShardKey() {
		return new HashMap<String, Object>() {{
			put("premium", premium);
			put("active", active);
			put("clientId", clientId);
			put("country", country.toString());
		}};
	}

	@Override
	@JsonIgnore
	public Query getShardQuery() {
		Query query = new Query(
				Criteria.where("country").is(country.toString()).and("active").is(active).and("premium").is(
						premium).and("clientId").is(clientId)

		);
		return query;
	}

	@Override
	@JsonIgnore
	public String getCollectionName() {
		return COLLECTION_NAME;
	}
}
