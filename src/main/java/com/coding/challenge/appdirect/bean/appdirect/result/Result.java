package com.coding.challenge.appdirect.bean.appdirect.result;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
public class Result {

	private boolean success;
	private String message;
	private String accountIdentifier;
	private ErrorCode errorCode;

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAccountIdentifier() {
		return accountIdentifier;
	}
	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
