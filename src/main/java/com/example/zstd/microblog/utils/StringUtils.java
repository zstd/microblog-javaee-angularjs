package com.example.zstd.microblog.utils;

/**
 * String utility methods.
 * 
 */
public class StringUtils {
	
	private StringUtils(){}
	
	public static final boolean isNullOrEmpty(String value) {
		if(value == null || value.trim().isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static final boolean exceedsLength(String value, int maxLength) {
		if(value == null || value.trim().isEmpty()) {
			return false;
		} else {
			return value.trim().length() > maxLength;
		}
	}
	
	public static final String trimIfNotNull(String value) {
		if(value != null) {
			return value.trim();
		}
		return value;
	}
	
	public static final String join(String joiner,String ...strings) {		
		if(strings != null && strings.length > 0) {
			StringBuilder result = new StringBuilder();
			for(int i = 0; i < strings.length; i++) {
				result.append(strings[i]);
				if(i != strings.length - 1) {
					result.append(joiner);
				}
			}
			return result.toString();
		}
		return null;
	}
	
	
}
