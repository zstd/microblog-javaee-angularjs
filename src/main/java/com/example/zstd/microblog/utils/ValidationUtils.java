package com.example.zstd.microblog.utils;


/**
 * Prefer using Google Guava preconditions.
 */
@Deprecated
public class ValidationUtils {
	
	public static final void checkArgument(boolean condition,RuntimeException exception) {
		if(condition) {
			throw exception;
		}
	}

}
