package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class JdbcWorker {
	
	
	
	public JdbcWorker(Connection connection, boolean closeConnection) {
		super();
		this.connection = connection;
		this.closeConnection = closeConnection;
	}
	
	public JdbcWorker(Connection connection) {
		this(connection,true);		
	}

	protected Connection connection;
	private boolean closeConnection;
	
	protected PreparedStatement ps = null;
    protected ResultSet rs = null;
	
	public Object executeWithResult() {
		Connection c = connection;
        try {
            return doWork();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RepositoryException("Exception at doWork",ex);
        } finally {
        	if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RepositoryException("Failed to close ResultSet",e);
                }
            }
        	if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RepositoryException("Failed to close PreparedStatement",e);
                }
            }
            if(closeConnection && c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    throw new RepositoryException("Failed to close Connection",e);
                }
            }
        }
	}
	
	protected abstract Object doWork() throws SQLException;
	

}
