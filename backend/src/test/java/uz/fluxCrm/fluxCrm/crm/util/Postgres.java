package uz.fluxCrm.fluxCrm.crm.util;

import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Postgres {
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:16-alpine"
    );

    public static class Initializer implements ApplicationContextInitializer <ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword() 
                ).applyTo(applicationContext);
        }
    };
}
