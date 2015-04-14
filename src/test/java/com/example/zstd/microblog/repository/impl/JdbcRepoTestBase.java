package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.conf.AppConfig;
import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.model.UserBuilder;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class JdbcRepoTestBase {

    protected JdbcUserRepo userRepo;

    private static final Logger LOG = Logger.getLogger(JdbcRepoTestBase.class.getName());

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                AppConfig.class, AppConfig.createAppConfig()
        ));
        String jdbcUrl = initDerbyDB();
        populateDB(jdbcUrl);

        setUpRepos();
    }

    /**
     * Populating database from flyway scripts.
     * @param jdbcUrl
     */
    private void populateDB(String jdbcUrl) {
        LOG.info("populateDB: " + jdbcUrl);
        if(jdbcUrl == null) {
            LOG.warning("JDBC url provided is null, skipping");
            return;
        }
        Flyway flyway = new Flyway();
        flyway.setDataSource(jdbcUrl, (String) null, (String) null);
        flyway.setLocations("classpath:db");
        flyway.migrate();
    }

    private String initDerbyDB() throws SQLException {
        // setting location of database files to temporary folder so database automatically cleaned
        try {
            String dbLocation = temporaryFolder.getRoot().getAbsolutePath();
            LOG.info("db location " + dbLocation);
            System.setProperty("derby.system.home",dbLocation);

            String jdbcUrl = ServiceLocator.getInstance().getService(AppConfig.class) .
                    getStringParam(AppConfig.Param.JDBC_URL);
            DriverManager.getConnection(jdbcUrl);
            return jdbcUrl;
        } catch(Throwable t) {
            LOG.log(Level.SEVERE,"failed to load derby driver",t);
        }
        return null;
    }

    protected abstract void setUpRepos();

    protected User givenUserExists(UserBuilder userBuilder) {
        Preconditions.checkState(userRepo != null, "User repository not initialized!");
        User user = userBuilder.build();
        userRepo.save(user);
        return user;
    }

    @After
    public void tearDown() throws Exception {
        LOG.info("tearDown");
        try {
            DriverManager.getConnection("jdbc:derby:microblogging;shutdown=true");
        } catch(SQLException se) {
            if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState()) ))) {
                // we got the expected exception
                LOG.info("Derby shut down normally");
            } else {
                LOG.log(Level.SEVERE,"Derby did not shut down normally",se);
            }
        }
    }
}