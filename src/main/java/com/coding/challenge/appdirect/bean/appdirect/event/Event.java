package com.coding.challenge.appdirect.bean.appdirect.event;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
public class Event {

	private EventType type;
	private Marketplace marketplace;
	private String flag;
	// @XmlElement(name="creator")
	private User creator;

	private Payload payload;
	private String returnUrl;

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Marketplace getMarketplace() {
		return marketplace;
	}

	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
