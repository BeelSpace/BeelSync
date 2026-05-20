package com.beelspace.beelsync.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDataBase {
    void create();
    Connection getConnection() throws SQLException;
}
