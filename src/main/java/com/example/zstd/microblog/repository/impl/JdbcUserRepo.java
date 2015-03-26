package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.repository.UserRepo;
import com.google.common.base.Joiner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcUserRepo extends BasicJdbcRepo implements UserRepo {
	
	private static final String FIND_BY_FIELD_QUERY = 
			"SELECT " + Joiner.on(",").join(User.DB_FIELDS) + " FROM users WHERE %s = ?";
	
	private static final String SAVE_QUERY = 
			"INSERT INTO users (" + Joiner.on(",").join(User.DB_FIELDS) + ") VALUES (?,?,?,?,?)";
	
	private static final String FOLLOWING_DATA_QUERY = 
			"select u.user_name, (select count(id) from follow_data fd where fd.following = u.user_name ) from users u";

	@Override
	public void save(final User blogUser) {
		Integer rowsUpdated = (Integer)new JdbcWorker(getConnection()) {
			@Override
			protected Object doWork() throws SQLException {
				ps = connection.prepareStatement(SAVE_QUERY);
				int index = 1;
				ps.setString(index++, blogUser.getUsername());
				ps.setString(index++, blogUser.getPassword());
				ps.setString(index++, blogUser.getNickname());
				ps.setString(index++, blogUser.getDescription());
				ps.setString(index++, blogUser.getPhotoUrl());
	            int updated = ps.executeUpdate();
				return updated;
			}
		}.executeWithResult();
		System.out.println("rows updated " + rowsUpdated);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<User> findByField(final String fieldName, final Object fieldValue) {
		List<User> users= (List<User>) new JdbcWorker(getConnection()) {
			@Override
			protected Object doWork() throws SQLException {
				String query = String.format(FIND_BY_FIELD_QUERY,fieldName);
				ps = connection.prepareStatement(query);
				ps.setObject(1, fieldValue);		        
	            rs = ps.executeQuery();
	            
	            List<User> result = new ArrayList<>();
	            while (rs.next()) {
	            	int index = 1;
	            	result.add(new User(rs.getString(index++), rs.getString(index++), rs.getString(index++),
	            			rs.getString(index++), rs.getString(index++)));
	            }
	            return result;
			}
		}.executeWithResult();
		
		return users;		
	}

	public static void main(String arg[]) throws SQLException {
		JdbcUserRepo repo = new JdbcUserRepo();
//		List<BlogUser> users = repo.findByField(BlogUser.DB_FIELD_USERNAME, "bob");
//		System.out.println("found user -->" + users);
//		if(!users.isEmpty()) {
//			BlogUser user = users.get(0);
//			BlogUser user2 = new BlogUser();
//			String prefix = System.currentTimeMillis() + "";
//			user2.setUsername(user.getUsername() + prefix);
//			user2.setPassword(user.getPassword() + prefix);
//			user2.setNickname(user.getNickname() + prefix);
//			user2.setDescription(user.getDescription());
//			user2.setPhotoUrl(user.getPhotoUrl() + prefix);
//			repo.save(user2);
//		}
//		users = repo.findByField(BlogUser.DB_FIELD_USERNAME, "bob");
//		System.out.println("found user -->" + users);
		System.out.println("repo -->" + repo.getUserFollowingData());
	}

	@Override
	public Map<String, Long> getUserFollowingData(){
		return (Map<String, Long>) new JdbcWorker(getConnection()) {		
			@Override
			protected Object doWork() throws SQLException {
				ps = connection.prepareStatement(FOLLOWING_DATA_QUERY);
				rs = ps.executeQuery();
	            
				Map<String, Long> result = new LinkedHashMap<>();		        
	            
				while (rs.next()) {
					int index = 1;
	            	result.put(rs.getString(index++), rs.getLong(index++));
	            }
	            return result;
			}
		}.executeWithResult();
	}

}
