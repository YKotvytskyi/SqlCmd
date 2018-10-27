package ua.com.juja.SqlCmd;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class JdbcMsSql {

    public static Connection connObj;
    public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=tutorialDb;integratedSecurity=true";

    public static void getDbConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL);
            if(connObj != null) {
                DatabaseMetaData metaObj = (DatabaseMetaData) connObj.getMetaData();
                System.out.println("Driver Name?= " + metaObj.getDriverName() + ", Driver Version?= " + metaObj.getDriverVersion() + ", Product Name?= " + metaObj.getDatabaseProductName() + ", Product Version?= " + metaObj.getDatabaseProductVersion());
                CallableStatement cstmt = null;
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getDbConnection();
    }
}