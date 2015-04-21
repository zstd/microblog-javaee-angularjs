package com.example.zstd.microblog.servlet.listeners;

import com.example.zstd.microblog.conf.AppConfig;
import com.example.zstd.microblog.repository.FollowDataRepo;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.repository.impl.JdbcFollowDataRepo;
import com.example.zstd.microblog.repository.impl.JdbcUserRepo;
import com.example.zstd.microblog.service.BlogPostService;
import com.example.zstd.microblog.service.FollowDataService;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class MicroblogServletContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(MicroblogServletContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("Starting initialization for microblog app");
        try {
            initializeApp();
        } catch(Throwable t) {
            LOG.log(Level.SEVERE,"Failed to initializeApp",t);
            throw t;
        }
    }

    private void initializeApp() {
        ImmutableMap.Builder<Class,Object> builder = new ImmutableBiMap.Builder<>();
        // app config
        builder.
                put(AppConfig.class, AppConfig.createAppConfig());
        // adding repos
        builder.
                put(UserRepo.class, new JdbcUserRepo()).
                put(FollowDataRepo.class, new JdbcFollowDataRepo());
        // adding services
        builder.
                put(BlogPostService.class, new BlogPostService()).
                put(FollowDataService.class, new FollowDataService());
        ServiceLocator.initialize(builder.build());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
