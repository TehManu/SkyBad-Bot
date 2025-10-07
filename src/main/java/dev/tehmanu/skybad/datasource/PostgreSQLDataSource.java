package dev.tehmanu.skybad.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author TehManu
 * @since 02.05.2025
 */
@Getter
public class PostgreSQLDataSource {
    private final HikariConfig config;
    private final HikariDataSource dataSource;

    public PostgreSQLDataSource() {
        this.config = new HikariConfig("app/config/database.properties");
        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}