package com.swift.userservice.utili;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonFormatConvertion {

	
	   public static String toJsonfromStringObject(Object ob) {
		   
		   String result = "";
		   try {
		   ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
	        result = objectWriter.writeValueAsString(ob);
		   }catch(JsonProcessingException ex) {
			   throw new RuntimeException(ex);
		   }
		   return result;
   }
}
