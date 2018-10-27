package ua.com.juja.SqlCmd;

import java.sql.Connection;

public class DatabaseManager {
    private static DatabaseManager instance = new DatabaseManager();
    private DatabaseManager(){}

    public static DatabaseManager getInstance(){
        return instance;
    }
    public static Connection getConnection() {return connection;}

    public static void setConnection(String database, String username, String password) {

        String JDBC_URL =
                String.format("\"jdbc:sqlserver://%1$s:%2$s;"
                        +"databaseName=%3$s;integratedSecurity=true"
                                +"",
                                Consts.SERVER_NAME,
                                Consts.SERVER_PORT,
                                database,
                                username,
                                password
                            );
    }

    private static Connection connection;

}

