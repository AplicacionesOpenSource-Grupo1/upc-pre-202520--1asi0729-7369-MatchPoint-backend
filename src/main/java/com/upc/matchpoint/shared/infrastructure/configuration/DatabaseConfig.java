package com.upc.matchpoint.shared.infrastructure.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Configuration
public class DatabaseConfig {

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
            try {
                int at = withoutScheme.indexOf('@');
                if (at > -1) {
                    String userInfo = withoutScheme.substring(0, at);
                    hostPart = withoutScheme.substring(at + 1);
                    int colon = userInfo.indexOf(':');
                    if (colon > -1) {
                        username = URLDecoder.decode(userInfo.substring(0, colon), "UTF-8");
                        password = URLDecoder.decode(userInfo.substring(colon + 1), "UTF-8");
                    } else {
                        username = URLDecoder.decode(userInfo, "UTF-8");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                // ignore, UTF-8 should always be supported
            }
            jdbcUrl = "jdbc:postgresql://" + hostPart;
        } else {
            // unknown scheme — attempt to use as JDBC URL
            jdbcUrl = raw;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        if (username != null && !username.isBlank()) config.setUsername(username);
        if (password != null && !password.isBlank()) config.setPassword(password);
        config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name", "org.postgresql.Driver"));

        this.hikariDataSource = new HikariDataSource(config);
        return this.hikariDataSource;
    }

    @PreDestroy
    public void close() {
        if (this.hikariDataSource != null) {
            this.hikariDataSource.close();
        }
    }
}
