package com.evampsaanga.FilterService.utils;

public class Constants {
	
	//Adding a private constructor to hide the implicit public one 
	 private Constants() {
		 throw new IllegalStateException("Utility class");
	}

	/**
	 * Salt Constants for SHA256
	 */
	public static final String SALT_STRING = "h38uydh48dji9kr3s";

	/**
	 * Constants for setting Headers or Requests
	 */

	public static final String CONTENT_TYPE_STRING = "application/json";
	public static final String TIMESTAMP_STRING = "timeStamp";
	public static final String REQUEST_CONFIG_STRING = "requestConfig";
	public static final String REQUEST_STRING = "requestString";

	/**
	 * Response Code Constants
	 */

	public static final String ERROR_RESPONSE_DESC = "Invalid Hash";
	public static final String ERROR_RESPONSE_CODE = "400";

}
