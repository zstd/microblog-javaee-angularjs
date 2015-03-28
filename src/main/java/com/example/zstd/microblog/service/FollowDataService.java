package com.example.zstd.microblog.service;

import com.example.zstd.microblog.model.FollowData;
import com.example.zstd.microblog.repository.FollowDataRepo;
import com.example.zstd.microblog.utils.StringUtils;
import com.example.zstd.microblog.utils.ValidationUtils;

import java.util.List;

public class FollowDataService {
	
	private FollowDataRepo repo = ServiceLocator.getInstance().getService(FollowDataRepo.class);
	
	public List<FollowData> getFollowerData(String username) {
        return repo.findByField("follower", username);

    }
	
	public List<FollowData> getFollowingData(String username) {
        return repo.findByField("following", username);
    }
	
	public FollowData addFollowerData(String follower,String following) {
		ValidationUtils.checkArgument(
                StringUtils.isNullOrEmpty(follower), new IllegalArgumentException("Follower is null or empty"));
		ValidationUtils.checkArgument(
				StringUtils.isNullOrEmpty(following), new IllegalArgumentException("Following is null or empty"));
		ValidationUtils.checkArgument(
				follower.equalsIgnoreCase(following), new IllegalArgumentException("Follower is null or empty"));

        return repo.save(new FollowData(follower,following));
    }
	
	public boolean deleteFollowerData(String follower,String following) {
		ValidationUtils.checkArgument(
				StringUtils.isNullOrEmpty(follower), new IllegalArgumentException("Follower is null or empty"));
		ValidationUtils.checkArgument(
				StringUtils.isNullOrEmpty(following), new IllegalArgumentException("Following is null or empty"));

        return repo.delete(follower,following) > 0;
    }
}
