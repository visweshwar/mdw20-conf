package com.paychex.mdw20.hrapplication.entity.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.UpdateResult;
import com.paychex.mdw20.hrapplication.entity.CustomMongoEntity;
import com.paychex.mdw20.hrapplication.entity.repository.CustomUpdateRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/*
    Author: Visweshwar Ganesh 
   created on 4/25/20 
*/
public class CustomUpdateRepositoryImpl implements CustomUpdateRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public boolean doUpdate(CustomMongoEntity pre, CustomMongoEntity post) {

		Query query = pre.getShardQuery();

		Update update = new Update();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String clientJson = objectMapper.writeValueAsString(post);
			Map<String, String> map = objectMapper.readValue(clientJson, Map.class);
			map.entrySet().stream().filter((kvp) -> !kvp.getKey().equals("_id")).forEach(
					kvp -> update.set(kvp.getKey(), kvp.getValue()));

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		UpdateResult updateResult = mongoTemplate.upsert(query, update, pre.getCollectionName());

		if (updateResult.getMatchedCount() > 0) {
			return true;
		}

		return false;
	}
}
