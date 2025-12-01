package com.upc.matchpoint.shared.infrastructure.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import jakarta.annotation.PreDestroy;
import javax.sql.DataSource;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
public class DatabaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);

    private HikariDataSource hikariDataSource;

    @Bean
    @Primary
    public DataSource dataSource(Environment env) {
        // Prefer explicit environment variable DATABASE_URL used by Render
        String raw = System.getenv("DATABASE_URL");
        if (raw == null || raw.isBlank()) {
            raw = env.getProperty("spring.datasource.url");
        }

        String jdbcUrl;
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");

        if (raw == null || raw.isBlank()) {
            // fallback to Spring Boot default behaviour (will use application.properties values)
            jdbcUrl = env.getProperty("spring.datasource.url");
        } else if (raw.startsWith("jdbc:")) {
            jdbcUrl = raw;
        } else if (raw.startsWith("postgres://") || raw.startsWith("postgresql://")) {
            // normalize: postgres://user:pass@host:port/dbname  -> jdbc:postgresql://host:port/dbname
            String withoutScheme = raw.replaceFirst("^postgres(?:ql)?://", "");
            String hostPart = withoutScheme;

            int at = withoutScheme.indexOf('@');
            if (at > -1) {
                String userInfo = withoutScheme.substring(0, at);
                hostPart = withoutScheme.substring(at + 1);
                int colon = userInfo.indexOf(':');
                if (colon > -1) {
                    username = URLDecoder.decode(userInfo.substring(0, colon), StandardCharsets.UTF_8);
                    password = URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8);
                } else {
                    username = URLDecoder.decode(userInfo, StandardCharsets.UTF_8);
                }
            }

            jdbcUrl = "jdbc:postgresql://" + hostPart;
        } else if (raw.startsWith("mysql://")) {
            // normalize: mysql://user:pass@host:port/dbname -> jdbc:mysql://host:port/dbname
            String withoutScheme = raw.replaceFirst("^mysql://", "");
            String hostPart = withoutScheme;

            int at = withoutScheme.indexOf('@');
            if (at > -1) {
                String userInfo = withoutScheme.substring(0, at);
                hostPart = withoutScheme.substring(at + 1);
                int colon = userInfo.indexOf(':');
                if (colon > -1) {
                    username = URLDecoder.decode(userInfo.substring(0, colon), StandardCharsets.UTF_8);
                    password = URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8);
                } else {
                    username = URLDecoder.decode(userInfo, StandardCharsets.UTF_8);
                }
            }

            // If hostPart doesn't include query params, append common MySQL params for timezone/SSL
            if (!hostPart.contains("?")) {
                jdbcUrl = "jdbc:mysql://" + hostPart + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            } else {
                jdbcUrl = "jdbc:mysql://" + hostPart;
            }
        } else {
            // unknown scheme — attempt to use as JDBC URL
            jdbcUrl = raw;
        }

        // If running in local profile and the configured jdbcUrl points to MySQL but driver is missing, fallback to H2 in-memory
        boolean isLocal = Arrays.asList(env.getActiveProfiles()).contains("local");
        if (isLocal && jdbcUrl != null && jdbcUrl.toLowerCase().startsWith("jdbc:mysql:")) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                LOGGER.warn("JDBC driver class 'com.mysql.cj.jdbc.Driver' not found on classpath and running with profile 'local' -> falling back to H2 in-memory DB for local development.");
                // switch to H2 in-memory DB to allow local startup without mysql connector
                jdbcUrl = "jdbc:h2:mem:matchpoint;DB_CLOSE_DELAY=-1;MODE=MySQL";
                username = "sa";
                password = "";
            }
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        if (username != null && !username.isBlank()) config.setUsername(username);
        if (password != null && !password.isBlank()) config.setPassword(password);
        // Select driver class dynamically based on JDBC URL when possible
        String driverProperty = env.getProperty("spring.datasource.driver-class-name");
        String candidateDriver = null;
        if (driverProperty != null && !driverProperty.isBlank()) {
            candidateDriver = driverProperty;
        } else if (jdbcUrl != null) {
            var lower = jdbcUrl.toLowerCase();
            if (lower.startsWith("jdbc:mysql:")) {
                candidateDriver = "com.mysql.cj.jdbc.Driver";
            } else if (lower.startsWith("jdbc:postgresql:")) {
                candidateDriver = "org.postgresql.Driver";
            } else if (lower.startsWith("jdbc:sqlserver:")) {
                candidateDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            }
        }

        if (candidateDriver != null) {
            try {
                // try to load the driver class first
                Class.forName(candidateDriver);
                config.setDriverClassName(candidateDriver);
                LOGGER.info("Using JDBC driver: {}", candidateDriver);
            } catch (ClassNotFoundException ex) {
                // Driver not on classpath; warn and continue letting Hikari auto-detect
                LOGGER.warn("JDBC driver class '{}' not found on classpath; leaving driver unset so Hikari can auto-detect. If you need this driver, add the dependency to pom.xml.", candidateDriver);
            }
        }

        try {
            this.hikariDataSource = new HikariDataSource(config);
            return this.hikariDataSource;
        } catch (RuntimeException rte) {
            // log diagnostic information to help debug driver/classpath issues
            try {
                StringBuilder drivers = new StringBuilder();
                java.util.Enumeration<java.sql.Driver> en = java.sql.DriverManager.getDrivers();
                while (en.hasMoreElements()) {
                    java.sql.Driver d = en.nextElement();
                    drivers.append(d.getClass().getName()).append("; ");
                }
                LOGGER.error("Failed to initialize HikariDataSource for jdbcUrl={}. Registered JDBC drivers: {}", jdbcUrl, drivers.toString());
                LOGGER.error("java.class.path={}", System.getProperty("java.class.path"));
            } catch (Exception ignore) {
                // ignore diagnostics failure
            }
            throw rte;
        }
    }

    @PreDestroy
    public void close() {
        if (this.hikariDataSource != null) {
            this.hikariDataSource.close();
        }
    }
}
