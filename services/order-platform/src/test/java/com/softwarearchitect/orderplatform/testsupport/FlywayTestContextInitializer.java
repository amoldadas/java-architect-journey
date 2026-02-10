package com.softwarearchitect.orderplatform.testsupport;

import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class FlywayTestContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        if (ctx.getBeanFactory().containsSingleton("flywayMigrated")) return;
        ctx.getBeanFactory().registerSingleton("flywayMigrated", Boolean.TRUE);

                Flyway.configure()
                .dataSource(
                    PostgresContainers.POSTGRES.getJdbcUrl(),
                    PostgresContainers.POSTGRES.getUsername(),
                    PostgresContainers.POSTGRES.getPassword()
                )
                .locations("classpath:db/migration")
                .cleanDisabled(true)
                .baselineOnMigrate(false)
                .load()
                .migrate();
    }
}


