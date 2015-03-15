package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.exception.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasicJdbcRepo {
	
	private static final String DELETE_QUERY = "DELETE FROM %s WHERE %s = ?";
	
	public BasicJdbcRepo() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InstantiationError("Failed to load JDBC driver");
		}
	}
	
	protected Connection getConnection(){
		String connectionUrl = "jdbc:derby://localhost:1527/microblogging;create=true";
        try {
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to connection ",e);
        }
    }
	
	protected Long getNextSequenceVal(final String sequenceName,Connection connection){
		return ((Long)new JdbcWorker(connection,false) {
			@Override
			protected Object doWork() throws SQLException {
				String query = "VALUES (NEXT VALUE FOR " + sequenceName + ")";
				System.out.println(query);
				ps = connection.prepareStatement(query);
				rs = ps.executeQuery();
				if(rs.next()) {
					return rs.getLong(1);
				} else {
					throw new RepositoryException("Failed to get next val for sequence " + sequenceName);
				}
			}
		}.executeWithResult());
		
	}
	
	protected int deleteById(final String tableName,final String idFieldName,final Object idFieldValue,
								Connection connection) throws SQLException {
		return ((int)new JdbcWorker(connection,false) {			
			@Override
			protected Object doWork() throws SQLException {
				String deleteQuery = String.format(DELETE_QUERY,tableName,idFieldName);
				ps = connection.prepareStatement(deleteQuery);
				ps.setObject(1, idFieldValue);
				return ps.executeUpdate();
			}
		}.executeWithResult());		
	}
	
	
}
