package com.example.zstd.microblog.servlet.listeners;

import com.example.zstd.microblog.conf.AppConfig;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.repository.impl.JdbcUserRepo;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.collect.ImmutableMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class MicroblogServletContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(MicroblogServletContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("Starting initialization for microblog app");
        ServiceLocator.initialize(ImmutableMap.<Class, Object>
                of(UserRepo.class, new JdbcUserRepo(),
                   AppConfig.class, AppConfig.createAppConfig()
                )
        );
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
