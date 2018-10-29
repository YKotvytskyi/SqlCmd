package ua.com.juja.SqlCmd;

import ua.com.juja.SqlCmd.DdTypes.DBTypeConstMssql;

import java.sql.Array;
import java.sql.Connection;
import java.util.Arrays;

public class JdbcMsSql {

    public static Connection connObj;
//    public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=tutorialDb;integratedSecurity=true";
    //public static String JDBC_URL = "jdbc:postgresql://localhost:5432;databaseName=testdb?user=postgres&password=admin";
    public static String JDBC_URL = "jdbc:postgresql://localhost:5432/testdb?user=postgres&password=admin";

    public static void getDbConnection() {
        //DatabaseManager db = DatabaseManager.getInstance(new DBTypeConstPosgree());
        //db.setConnection("testdb","postgres","admin");

        DatabaseManager db = DatabaseManager.getInstance(new DBTypeConstMssql());
        db.setConnection("ImportProcessing","postgres","admin");

        System.out.println(db.getConnection().toString());
        String tableName = "aProject";
        System.out.println(db.tableExist(tableName));
        String[][] rowSet = db.tableFind(tableName);
        if (rowSet != null) {
            for (String[] row: rowSet) {
                System.out.println(Arrays.toString(row));
            }
        }
    }

    public static void main(String[] args) {
        getDbConnection();
    }
}