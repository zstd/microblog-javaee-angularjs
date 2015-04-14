package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.repository.UserRoleRepo;

import java.sql.SQLException;
import java.util.logging.Logger;

public class JdbcUserRoleRepo extends BasicJdbcRepo implements UserRoleRepo {

    private static final Logger LOG = Logger.getLogger(JdbcUserRepo.class.getName());
	
	private static final String SAVE_QUERY = 
			"INSERT INTO user_roles (user_name,role_name) VALUES (?,?)";

	@Override
	public void add(final String username,final String role) {
		Integer rowsUpdated = (Integer)new JdbcWorker(getConnection()) {
			@Override
			protected Object doWork() throws SQLException {
				ps = connection.prepareStatement(SAVE_QUERY);
				int index = 1;
				ps.setString(index++, username);
				ps.setString(index++, role);
				int updated = ps.executeUpdate();
				return updated;
			}
		}.executeWithResult();
		LOG.fine("rows updated " + rowsUpdated);
	}

}
