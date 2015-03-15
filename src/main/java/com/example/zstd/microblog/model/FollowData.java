package com.example.zstd.microblog.model;

/**
 * Model class representing one follower-following relation.
 */
public class FollowData {
	
	public static final String DB_FIELD_ID = "id";
	public static final String DB_FIELD_FOLLOWER = "follower";
	public static final String DB_FIELD_FOLLOWING = "following";
	
	public static final String[] DB_FIELDS = new String[]{
						FollowData.DB_FIELD_ID,FollowData.DB_FIELD_FOLLOWER,FollowData.DB_FIELD_FOLLOWING};
	
	private Long id;
	private String follower;
	private String following;
	
	public FollowData(Long id, String follower, String following) {
		super();
		this.id = id;
		this.follower = follower;
		this.following = following;
	}
	
	public FollowData(String follower, String following) {
		this(null,follower,following);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFollower() {
		return follower;
	}
	public void setFollower(String follower) {
		this.follower = follower;
	}
	public String getFollowing() {
		return following;
	}
	public void setFollowing(String following) {
		this.following = following;
	}

	@Override
	public String toString() {
		return "FollowData [id=" + id + ", follower=" + follower
				+ ", following=" + following + "]";
	}
}
