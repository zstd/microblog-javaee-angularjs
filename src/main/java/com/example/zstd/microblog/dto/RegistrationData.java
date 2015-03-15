package com.example.zstd.microblog.dto;

import com.google.common.base.Strings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple DTO object used at user registration.
 */
public class RegistrationData {
	
	private static final String EMPTY_ARG_ERROR_TEMPLATE = "Field '%s' must be not empty";
	private static final String INVALID_URL_ERROR_TEMPLATE = "Field '%s' is not valid URL";
	
	/**
	 * Main constructor with all fields provided.	 
	 */
    public RegistrationData(String username, String nickname, String password,
                     String passwordCheck, String description, String photoUrl) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.passwordCheck = passwordCheck;
		this.description = description;
		this.photoUrl = photoUrl;
	}

	private final String username;
	private final String nickname;
	private final String password;
	private final String passwordCheck;
	private final String description;
	private final String photoUrl;
	
	public String getUsername() {
		return username;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordCheck() {
		return passwordCheck;
	}

	public String getDescription() {
		return description;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}
	/**
	 * Validates the content of this object for some rules.
	 * @return - list of violations as strings or empty if no violations.
	 */
	public List<String> validate() {
		List<String> validationMessages = new ArrayList<>();
		
		checkEmpty(username,String.format(EMPTY_ARG_ERROR_TEMPLATE, "username"),validationMessages);
		checkEmpty(nickname,String.format(EMPTY_ARG_ERROR_TEMPLATE, "nickname"),validationMessages);
		checkEmpty(password,String.format(EMPTY_ARG_ERROR_TEMPLATE, "password"),validationMessages);
		checkEmpty(passwordCheck,String.format(EMPTY_ARG_ERROR_TEMPLATE, "passwordCheck"),validationMessages);
		
		checkPasswordAndPasswordCheck(validationMessages);
		
		checkPhotoUrlValid(validationMessages);
		return validationMessages;
	}
	
	/**
	 * Checks that provided photo url is valid.
	 * @param validationMessages - container for validataion error messages.
	 */
	private void checkPhotoUrlValid(List<String> validationMessages) {
		try {
			new URL(photoUrl);
		} catch (MalformedURLException e) {
			validationMessages.add(String.format(INVALID_URL_ERROR_TEMPLATE, photoUrl));
		}		
	}
	
	/**
	 * Checks that password and its repeat are equals.
	 * @param validationMessages - container for validataion error messages.
	 */
	private void checkPasswordAndPasswordCheck(List<String> validationMessages) {
		if(password.equals(passwordCheck)) {
			validationMessages.add("Password and password check are not equal");
		}		
	}
	
	/**
	 * Checks that provided string is not empty.
	 * @param value - incoming string
	 * @param validationMessages - container for validataion error messages.
	 */
	private void checkEmpty(String value,String message,List<String> validationMessages) {
		if(Strings.isNullOrEmpty(value)) {
			validationMessages.add(message);
		}		
	}

}
