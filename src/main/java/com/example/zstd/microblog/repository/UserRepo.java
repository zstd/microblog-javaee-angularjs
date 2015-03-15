package com.example.zstd.microblog.repository;

import com.example.zstd.microblog.model.User;

import java.util.List;
import java.util.Map;


public interface UserRepo {
	
	void save(User blogUser);
	
	List<User> findByField(String fieldName,Object fieldValue);
	
	Map<String,Long> getUserFollowingData();
	
}
