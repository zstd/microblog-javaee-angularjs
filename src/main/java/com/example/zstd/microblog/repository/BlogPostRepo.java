package com.example.zstd.microblog.repository;

import com.example.zstd.microblog.model.BlogPost;

import java.sql.SQLException;
import java.util.List;


/**
 * Class for accessing {@link BlogPost} storage.
 */
public interface BlogPostRepo {
	
	/**
	 * Saves post into storage.
	 * @param blogPost - container for save data.
	 * @return - newly created post
	 * @throws SQLException - in case of storage errors.
	 */
	BlogPost save(BlogPost blogPost);
	
	/**
	 * Loads list of all {@link BlogPost} from storage.
	 * @return - list
	 * @throws SQLException - in case of storage errors.
	 */
	List<BlogPost> listAll();
	
}
