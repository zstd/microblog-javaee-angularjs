package com.example.zstd.microblog.service;

public class RegistrationService {
	
	public static final String MAIN_ROLE = "CHAT_USER";
	
	private static final String DUPLICATE_USERNAME_ERROR_TEMPLATE = "Blog user with username '%s' already exists";
	private static final String DUPLICATE_NICKNAME_ERROR_TEMPLATE = "Blog user with nickname '%s' already exists";
	
	//private UserRoleRepo userRoleRepo = new JdbcUserRoleRepo();
	//private UserRepo userRepo = new JdbcUserRepo();
	
	/**
	 * Creates blog user from registration data
	 * @param data - data for creating user, must be valid according to the RegistrationData.validate specification.
	 * @return newly created blog user
	 * @throws RegistrationException 
	 */
//	public BlogUser create(RegistrationData data) throws RegistrationException {
//		checkUsernameExists(data);
//		checkNickNameExists(data);
//
//		BlogUser newUser = createFromRegistrationData(data);
//		try {
//			userRepo.save(newUser);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new RegistrationException("Failed to create user " + newUser);
//		}
//		try {
//			userRoleRepo.add(newUser.getUsername(), MAIN_ROLE);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new RegistrationException("Failed to create user ROLE " + newUser);
//		}
//		return newUser;
//	}

//	private void checkNickNameExists(RegistrationData data) throws RegistrationException {
//		List<BlogUser> blogUser = null;
//		if(!StringUtils.isNullOrEmpty(data.getNickname())) {
//			try {
//				blogUser = userRepo.findByField(BlogUser.DB_FIELD_NICKNAME, data.getNickname());
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new RuntimeException(e.getMessage());
//			}
//		}
//		if(blogUser != null && !blogUser.isEmpty()) {
//			throw new RegistrationException(String.format(DUPLICATE_NICKNAME_ERROR_TEMPLATE, data.getNickname()));
//		}
//	}

//	private BlogUser createFromRegistrationData(RegistrationData data) {
//		BlogUser blogUser = new BlogUser();
//		blogUser.setUsername(data.getUsername());
//		blogUser.setNickname(data.getNickname());
//		blogUser.setPassword(data.getPassword());
//		blogUser.setDescription(data.getDescription());
//		blogUser.setPhotoUrl(data.getPhotoUrl());
//		return blogUser;
//	}
//
//	private void checkUsernameExists(RegistrationData data) throws RegistrationException {
//		List<BlogUser> blogUser = null;
//		if(!StringUtils.isNullOrEmpty(data.getUsername())) {
//			try {
//				blogUser = userRepo.findByField(BlogUser.DB_FIELD_USERNAME, data.getUsername());
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new RuntimeException(e.getMessage());
//			}
//		}
//		if(blogUser != null && !blogUser.isEmpty()) {
//			throw new RegistrationException(String.format(DUPLICATE_NICKNAME_ERROR_TEMPLATE, data.getNickname()));
//		}
//	}

}
