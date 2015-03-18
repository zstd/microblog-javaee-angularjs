package com.example.zstd.microblog.service;

import com.example.zstd.microblog.dto.RegistrationData;
import com.example.zstd.microblog.exception.RegistrationException;
import com.example.zstd.microblog.exception.RepositoryException;
import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.repository.UserRoleRepo;
import com.google.common.base.Strings;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationService {

    private static final Logger LOG = Logger.getLogger(RegistrationService.class.getName());
	
	public static final String MAIN_ROLE = "CHAT_USER";
	
	private static final String DUPLICATE_USERNAME_ERROR_TEMPLATE = "Blog user with username '%s' already exists";
	private static final String DUPLICATE_NICKNAME_ERROR_TEMPLATE = "Blog user with nickname '%s' already exists";
	
	private UserRoleRepo userRoleRepo /*= new JdbcUserRoleRepo()*/;

    private UserRepo userRepo /*=new JdbcUserRepo()*/;
	
	/**
	 * Creates blog user from registration data
	 * @param data - data for creating user, must be valid according to the RegistrationData.validate specification.
	 * @return newly created blog user
	 * @throws RegistrationException 
	 */
	public User create(RegistrationData data) throws RegistrationException {
		checkUserWithFieldExists(User.DB_FIELD_USERNAME,data.getUsername(),DUPLICATE_USERNAME_ERROR_TEMPLATE);
        checkUserWithFieldExists(User.DB_FIELD_NICKNAME,data.getNickname(),DUPLICATE_NICKNAME_ERROR_TEMPLATE);

		User newUser = createFromRegistrationData(data);
		try {
			userRepo.save(newUser);
		} catch (RepositoryException e) {
            LOG.log(Level.SEVERE,"Failed to save user",e);
			throw new RegistrationException("Failed to create user " + newUser);
		}
		try {
			userRoleRepo.add(newUser.getUsername(), MAIN_ROLE);
		} catch (RepositoryException e) {
            LOG.log(Level.SEVERE, "Failed to save user role", e);
			throw new RegistrationException("Failed to create user ROLE " + newUser);
		}
		return newUser;
	}

	private User createFromRegistrationData(RegistrationData data) {
		User blogUser = new User();
		blogUser.setUsername(data.getUsername());
		blogUser.setNickname(data.getNickname());
		blogUser.setPassword(data.getPassword());
		blogUser.setDescription(data.getDescription());
		blogUser.setPhotoUrl(data.getPhotoUrl());
		return blogUser;
	}

	private void checkUserWithFieldExists(String fieldName,String fieldValue,String errorTemplate) {
        List<User> blogUser = null;
        if(!Strings.isNullOrEmpty(fieldValue)) {
            blogUser = userRepo.findByField(fieldName, fieldValue);
        }
        if(blogUser != null && !blogUser.isEmpty()) {
            throw new RegistrationException(String.format(errorTemplate, fieldValue));
        }
    }

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void setUserRoleRepo(UserRoleRepo userRoleRepo) {
        this.userRoleRepo = userRoleRepo;
    }
}
