package com.paychex.mdw20.hrapplication.entity;

import java.util.Map;
import org.springframework.data.mongodb.core.query.Query;

/*
    Author: Visweshwar Ganesh 
   created on 4/26/20 
*/
public interface CustomMongoEntity {
	Map<String, Object> getShardKey();

	Query getShardQuery();

	String getCollectionName();
}
