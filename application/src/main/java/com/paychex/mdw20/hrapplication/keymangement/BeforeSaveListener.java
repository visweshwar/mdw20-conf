package com.paychex.mdw20.hrapplication.keymangement;

import com.paychex.mdw20.hrapplication.entity.Client;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

/*
    Author: Visweshwar Ganesh 
   created on 4/25/20 
*/
@Component("beforeSaveListener")
public class BeforeSaveListener extends AbstractMongoEventListener<Client> {
	@Override
	public void onBeforeSave(BeforeSaveEvent<Client> event) {

		System.out.println(event.toString());
	}
}
