package com.coding.challenge.appdirect.bean.appdirect.event;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="marketplace")
public class Marketplace {
	
	private String baseUrl;
	private String partner;
	
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	
	

}
