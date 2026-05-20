package com.beelspace.beelsync.database.mysql;

import com.beelspace.beelsync.database.IDataBase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlDB implements IDataBase {

    private final HikariDataSource hikariDataSource;

    public MySqlDB(String host, int port, String database, String user, String password) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public void create() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS profiles(" +
                    " nick_player VARCHAR(16) PRIMARY KEY, " +
                    " inventory TEXT, " +
                    " potion_effects TEXT, " +
                    " health DOUBLE, " +
                    " health_scale DOUBLE, " +
                    " max_health DOUBLE, " +
                    " experience INT, " +
                    " food_level INT, " +
                    " saturation FLOAT, " +
                    " exhaustion FLOAT)");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
