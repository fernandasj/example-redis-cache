package io.github.fernandasj.example.redis.postgresql.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author fernanda
 */
public class ConnectionFactory {
    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://127.0.0.1:5432/bdnc-store";
        String user = "postgres";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }
}
