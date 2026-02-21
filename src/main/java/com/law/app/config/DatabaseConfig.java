package com.law.app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseConfig {

    @Value("${law.app.db.host:localhost}")
    private String host;

    @Value("${law.app.db.port:5432}")
    private int port;

    @Value("${law.app.db.name:lawapp_db}")
    private String databaseName;

    @Value("${spring.datasource.username:postgres}")
    private String username;

    @Value("${spring.datasource.password:postgres}")
    private String password;

    @Bean
    public DataSource dataSource() {
        ensureDatabaseExists();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + databaseName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private void ensureDatabaseExists() {
        if (!databaseName.matches("[A-Za-z0-9_]+")) {
            throw new IllegalStateException("Invalid database name: " + databaseName);
        }

        String adminUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";
        String sql = "CREATE DATABASE \"" + databaseName + "\"";

        try (Connection connection = DriverManager.getConnection(adminUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException ex) {
            // 42P04 = duplicate_database (already exists)
            if (!"42P04".equals(ex.getSQLState())) {
                throw new IllegalStateException("Failed to create database: " + databaseName, ex);
            }
        }
    }
}
