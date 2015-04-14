package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.conf.AppConfig;
import com.example.zstd.microblog.exception.RepositoryException;
import com.example.zstd.microblog.service.ServiceLocator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicJdbcRepo {

    private static final Logger LOG = Logger.getLogger(BasicJdbcRepo.class.getName());

	private static final String DELETE_QUERY = "DELETE FROM %s WHERE %s = ?";
    private static final String NEXT_SEQ_VAL_TEMPLATE = "VALUES (NEXT VALUE FOR %s)";
	
	public BasicJdbcRepo() {
		try {
            /**
             * Assuming that app works only on Derby.
             */
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e) {
			LOG.log(Level.SEVERE,"Failed to load JDBC driver class",e);
			throw new InstantiationError("Failed to load JDBC driver");
		}
	}
	
	protected Connection getConnection(){
		String connectionUrl = ServiceLocator.getInstance().getService(AppConfig.class).
                getStringParam(AppConfig.Param.JDBC_URL);
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
				String query = String.format(NEXT_SEQ_VAL_TEMPLATE,sequenceName);
				LOG.finest(query);
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

    protected int deleteById(final String tableName,final String idFieldName,final Object idFieldValue)
            throws SQLException {
        return deleteById(tableName,idFieldName,idFieldValue,getConnection());
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
