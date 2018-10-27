package ua.com.juja.SqlCmd;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class DatabaseManager {

    private static DatabaseManager instance = new DatabaseManager();

    private static DBTypeConst dbType = new DBTypeConstPosgree();

    private DatabaseManager(){}

    public void setDbType(DBTypeConst dbType){
        this.dbType = dbType;
    }

    public static Connection connObj;

    public static DatabaseManager getInstance(){
        return instance;
    }

    public Connection getConnection() {return connection;}

    public void setConnection(String database, String username, String password) {
        String JDBC_URL = getConnectionString(database,username,password);
        try {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("org.postgresql.Driver");
            connObj = DriverManager.getConnection(JDBC_URL);
            if(connObj != null) {
                DatabaseMetaData metaObj = (DatabaseMetaData) connObj.getMetaData();
                System.out.println("Driver Name?= " + metaObj.getDriverName() + ", Driver Version?= "
                        + metaObj.getDriverVersion() + ", Product Name?= "
                        + metaObj.getDatabaseProductName()
                        + ", Product Version?= " + metaObj.getDatabaseProductVersion());
                CallableStatement cstmt = null;
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    private String getConnectionString(String database, String username, String password){
        return dbType.getConnectionString(database, username, password);
    }
    private Connection connection;

}

