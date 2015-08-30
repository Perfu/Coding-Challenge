package com.coding.challenge.appdirect.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

	@Id
	private String uuid;
	private String country;
	private String email;
	private String name;
	private String phoneNumber;
	private String edition;
	private String maxUsers;

	public Account() {
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(String maxUsers) {
		this.maxUsers = maxUsers;
	}

	@Override
	public String toString() {
		return String.format("id : %s , name : %s", uuid, name);
	}

}
