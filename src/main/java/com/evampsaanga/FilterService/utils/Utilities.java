package com.evampsaanga.FilterService.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Muhammad Fazeel Utilities Class Defines All the Utility Methods and
 *         Attributes which needs to use as static
 */

public class Utilities {
	
	//Adding a private constructor to hide the implicit public one 
	 private Utilities() {
		 throw new IllegalStateException("Utility class");
	}
	 
	public static String printObject(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}
