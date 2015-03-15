package com.example.zstd.microblog.repository;

import com.example.zstd.microblog.model.FollowData;

import java.util.List;


public interface FollowDataRepo {
	
	FollowData save(FollowData followData);
	
	List<FollowData> findByField(String fieldName,Object fieldValue);
	
	int delete(String follower,String following);
	
}
