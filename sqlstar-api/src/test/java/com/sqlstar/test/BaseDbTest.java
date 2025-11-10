package com.sqlstar.test;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MSSQLServerContainer;

/**
 * Single shared MSSQL Testcontainers instance for the entire test JVM.
 * We avoid @ServiceConnection and manage properties via @DynamicPropertySource
 * to prevent port churn between test classes.
 */
public abstract class BaseDbTest {

    // Single JVM-wide container instance
    static final MSSQLServerContainer<?> MSSQL =
            new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
                    .acceptLicense();

    static {
        MSSQL.start();
    }

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        // Ensure Hibernate can connect using the container's actual JDBC URL
        registry.add("spring.datasource.url", () -> MSSQL.getJdbcUrl() + ";encrypt=false;trustServerCertificate=true");
        registry.add("spring.datasource.username", MSSQL::getUsername);
        registry.add("spring.datasource.password", MSSQL::getPassword);
        // Explicit dialect for clarity
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.SQLServerDialect");
    }
}
