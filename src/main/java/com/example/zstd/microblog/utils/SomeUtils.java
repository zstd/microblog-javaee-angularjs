package com.example.zstd.microblog.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Different utility methods.
 * 
 */
public class SomeUtils {
	public static final String getFirst(String name,Map<String,String[]> map,String defaultValue) {
		String[] arr = map.get(name);
		if(arr != null && arr.length > 0) {
			return arr[0];
		}
		return defaultValue;
	}
	
	public static final String getFirst(String name,Map<String,String[]> map) {
		return getFirst(name, map,null);
	}
	
	public static final String extractPayloadAsString(HttpServletRequest request) throws IOException {
		StringBuilder result = new StringBuilder();
		try(BufferedReader reader = request.getReader()) {
			String line = null;
			while( (line = reader.readLine()) != null) {
				result.append(line);
			}
		}
		return result.toString();
	}
	
	
}
