package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.model.FollowData;
import com.example.zstd.microblog.repository.FollowDataRepo;
import com.google.common.base.Joiner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcFollowDataRepo extends BasicJdbcRepo implements FollowDataRepo {
	
	private static final String TABLE_NAME = "follow_data";
	private static final String SEQ_NAME = "follow_data_seq";
	
	private static final String FIND_BY_FIELD_QUERY = 
			"SELECT " + Joiner.on(",").join(FollowData.DB_FIELDS) + " FROM " + TABLE_NAME +" WHERE %s = ?";
	
	private static final String SAVE_QUERY = 
			"INSERT INTO "+ TABLE_NAME + " (" + Joiner.on(",").join(", ", FollowData.DB_FIELDS) + ") "
					+ "VALUES (?,?,?)";
	
	private static final String DELETE_QUERY = 
			"DELETE FROM "+ TABLE_NAME + " WHERE " + FollowData.DB_FIELD_FOLLOWER + 
			" = ? AND " + FollowData.DB_FIELD_FOLLOWING + " = ?";

	@Override
	public FollowData save(final FollowData followData) {
		return (FollowData) new JdbcWorker(getConnection()) {			
			@Override
			protected Object doWork() throws SQLException {
				System.out.println(SAVE_QUERY);
				Long id = getNextSequenceVal(SEQ_NAME, connection);
				ps = connection.prepareStatement(SAVE_QUERY);
				int index = 1;
				ps.setLong(index++, id);
				ps.setString(index++, followData.getFollower());
				ps.setString(index++, followData.getFollowing());
				int updated = ps.executeUpdate();
				
				return updated > 0 ? new FollowData(id,followData.getFollower(),followData.getFollowing()) : null;
			}
		}.executeWithResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FollowData> findByField(final String fieldName, final Object fieldValue) {
		List<FollowData> users= (List<FollowData>) new JdbcWorker(getConnection()) {		
			@Override
			protected Object doWork() throws SQLException {
				String query = String.format(FIND_BY_FIELD_QUERY,fieldName);
				ps = connection.prepareStatement(query);
				ps.setObject(1, fieldValue);		        
	            rs = ps.executeQuery();
	            
	            List<FollowData> result = new ArrayList<>();		        
	            while (rs.next()) {
	            	int index = 1;
	            	result.add(new FollowData(rs.getLong(index++), rs.getString(index++), rs.getString(index++)));
	            }
	            return result;
			}
		}.executeWithResult();
		
		return users;		
	}
	
	@Override
	public int delete(final String follower,final String following) {
		return (int) new JdbcWorker(getConnection()) {			
			@Override
			protected Object doWork() throws SQLException {
				System.out.println(DELETE_QUERY);
				ps = connection.prepareStatement(DELETE_QUERY);
				int index = 1;
				ps.setString(index++, follower);
				ps.setString(index++, following);
				int updated = ps.executeUpdate();
				
				return updated;
			}
		}.executeWithResult();
	}

	public static void main(String arg[]) throws SQLException {
		JdbcFollowDataRepo repo = new JdbcFollowDataRepo();
		FollowData data = new FollowData(null, "user1", "user2");
		FollowData saved = repo.save(data);
		System.out.println("saved " + saved);
		
		
		List<FollowData> users = repo.findByField(FollowData.DB_FIELD_FOLLOWER, "user1");
		System.out.println("found user -->" + users);
		repo.delete(users.get(0).getFollower(),users.get(0).getFollowing());	
		users = repo.findByField(FollowData.DB_FIELD_FOLLOWER, "user1");
		System.out.println("found user -->" + users);
	}

	

}
