package com.paychex.mdw20.hrapplication.model;

import com.paychex.mdw20.hrapplication.entity.Countries;

/*
    Author: Visweshwar Ganesh 
   created on 4/25/20 
*/
public class ClientModel {
	private String clientId;
	private String clientName;
	private boolean premium;
	private String address;
	private Countries country;
	private boolean active;

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

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
