package com.example.zstd.microblog.utils;


public class ValidationUtils {
	
	public static final void checkArgument(boolean condition,RuntimeException exception) {
		if(condition) {
			throw exception;
		}
	}

}
