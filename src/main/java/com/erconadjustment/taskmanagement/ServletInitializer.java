package com.erconadjustment.taskmanagement;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Required so the packaged WAR can be deployed into an external servlet
 * container (JBoss / WildFly) instead of only running via "java -jar".
 * JBoss looks for this hook (a WebApplicationInitializer) when it deploys
 * the WAR from standalone/deployments.
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TaskManagementApplication.class);
    }
}
