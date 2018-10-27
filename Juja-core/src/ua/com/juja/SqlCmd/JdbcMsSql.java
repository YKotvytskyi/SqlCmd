package ua.com.juja.SqlCmd;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class JdbcMsSql {

    public static Connection connObj;
//    public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=tutorialDb;integratedSecurity=true";
    //public static String JDBC_URL = "jdbc:postgresql://localhost:5432;databaseName=testdb?user=postgres&password=admin";
    public static String JDBC_URL = "jdbc:postgresql://localhost:5432/testdb?user=postgres&password=admin";


    public static void getDbConnection() {
        DatabaseManager db = DatabaseManager.getInstance();
        db.setDbType(new DBTypeConstPosgree());
        db.setConnection("testdb","postgres","admin");
    }

    public static void main(String[] args) {
        getDbConnection();
    }
}