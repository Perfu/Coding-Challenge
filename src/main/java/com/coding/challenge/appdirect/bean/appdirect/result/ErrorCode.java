package com.coding.challenge.appdirect.bean.appdirect.result;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Enum representing Error Code
 * 
 * @author azieba
 *
 * {@link http://info.appdirect.com/developers/docs/event_references/api_error_codes}
 */
@XmlEnum
public enum ErrorCode {

	USER_ALREADY_EXISTS,
	USER_NOT_FOUND,
	ACCOUNT_NOT_FOUND,
	MAX_USERS_REACHED,
	UNAUTHORIZED,
	OPERATION_CANCELED,
	CONFIGURATION_ERROR,
	INVALID_RESPONSE,
	UNKNOWN_ERROR,
	PENDING;
}
