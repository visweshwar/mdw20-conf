package com.paychex.mdw20.hrapplication.entity.repository;

import com.paychex.mdw20.hrapplication.entity.CustomMongoEntity;

public interface CustomUpdateRepository {
	boolean doUpdate(CustomMongoEntity pre, CustomMongoEntity post);
}
