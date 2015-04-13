package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.model.BlogPost;
import com.example.zstd.microblog.repository.BlogPostRepo;
import com.google.common.base.Joiner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JdbcBlogPostRepo extends BasicJdbcRepo implements BlogPostRepo {
	
	private static final String TABLE_NAME = "posts";
	
	private static final String SAVE_QUERY = 
			"INSERT INTO " + TABLE_NAME +" (" + Joiner.on(",").join(BlogPost.DB_FIELDS) + ") VALUES (?,?,?,?,?,?)";
	
	private static final String SELECT_ALL_QUERY = 
			"SELECT " + Joiner.on(",").join(BlogPost.DB_FIELDS) +" FROM " + TABLE_NAME;
	
	private static final String SEQ_NAME = "posts_seq";

	@Override
	public BlogPost save(final BlogPost blogPost) {
		return (BlogPost)new JdbcWorker(getConnection()) {			
			@Override
			protected Object doWork() throws SQLException {
				Long id = getNextSequenceVal(SEQ_NAME, connection);
				ps = connection.prepareStatement(SAVE_QUERY);
				int index = 1;
				ps.setLong(index++, id);
				ps.setString(index++, blogPost.getCreator());
				ps.setString(index++, blogPost.getMessage());
				ps.setString(index++, blogPost.getTopics());
				ps.setString(index++, blogPost.getMentions());
				Date created = blogPost.getCreated() != null ? blogPost.getCreated() : new Date();
				ps.setTimestamp(index++,new java.sql.Timestamp(created.getTime()));
	            int updated = ps.executeUpdate();
				return updated > 0 ? new BlogPost(id,blogPost.getCreator(),blogPost.getMessage(),
						blogPost.getTopics(),blogPost.getMentions(),created) : null;
			}
		}.executeWithResult();				
	}
	
	@Override
	public List<BlogPost> listAll(){
		return (List<BlogPost>) new JdbcWorker(getConnection()) {		
			@Override
			protected Object doWork() throws SQLException {
				ps = connection.prepareStatement(SELECT_ALL_QUERY);
				rs = ps.executeQuery();
	            
	            List<BlogPost> result = new ArrayList<>();		        
	            while (rs.next()) {
	            	int index = 1;
	            	result.add(new BlogPost(rs.getLong(index++), rs.getString(index++), rs.getString(index++), 
	            			rs.getString(index++), rs.getString(index++),rs.getTimestamp(index++)));
	            }
	            return result;
			}
		}.executeWithResult();
	}
	
	public static void main(String arg[]) throws SQLException {
		JdbcBlogPostRepo repo = new JdbcBlogPostRepo();
		
		BlogPost post = new BlogPost(null,"bob","message of @bob in #topic1 is here","topic1","bob",null);
		repo.save(post);
		
		System.out.println(repo.listAll());
		
		
	}



}
