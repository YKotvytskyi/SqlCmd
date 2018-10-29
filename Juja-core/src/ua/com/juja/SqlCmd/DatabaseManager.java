package ua.com.juja.SqlCmd;

import ua.com.juja.SqlCmd.DdTypes.DBTypeConst;
import ua.com.juja.SqlCmd.DdTypes.DBTypeConstPosgree;

import java.sql.*;


public class DatabaseManager {

    private static DatabaseManager instance = new DatabaseManager();

    private static DBTypeConst dbType = new DBTypeConstPosgree();

    private DatabaseManager(){}

    public void setDbType(DBTypeConst dbType){
        if (this.dbType.getClass() != dbType.getClass()){
            ConnObj = null;
            this.dbType = dbType;
        }
    }

    public static Connection ConnObj;

    public static DatabaseManager getInstance(){
        return instance;
    }

    public static DatabaseManager getInstance(DBTypeConst dbType){
        instance.setDbType(dbType);
        return instance;
    }

    public Connection getConnection() {return ConnObj;}

    public void setConnection(String database, String username, String password) {
        String JDBC_URL = getConnectionString(database,username,password);
        try {
            Class.forName(dbType.DriverClassName);
            ConnObj = DriverManager.getConnection(JDBC_URL);
            if(ConnObj != null) {
                DatabaseMetaData metaObj = (DatabaseMetaData) ConnObj.getMetaData();
                System.out.println("Driver Name?= " + metaObj.getDriverName() + ", Driver Version?= "
                        + metaObj.getDriverVersion() + ", Product Name?= "
                        + metaObj.getDatabaseProductName()
                        + ", Product Version?= " + metaObj.getDatabaseProductVersion());
                CallableStatement cstmt = null;
                Statement stmt = ConnObj.createStatement();

            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    private String getConnectionString(String database, String username, String password){
        return dbType.getConnectionString(database, username, password);
    }

    public String list(){
        String result = null;
        try {
            Statement stmt = ConnObj.createStatement();
            ResultSet rs = stmt.executeQuery(dbType.list());
            result = "[";
            while (rs.next()){
                result += rs.getString(1) + ",";
            }
            if (result.length() > 1){
                result = result.substring(0,result.length() - 1);
            }
            result += "]";
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return result;
    }

    boolean tableExist(String tableName){
        try {
            PreparedStatement stmt = ConnObj.prepareStatement(dbType.checkTableExist());
            stmt.setString(1, tableName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }

    private String[][] getRowsFromTable(String tableName){
        String[][] result = null;
        try {
            int MAX_ROWS = 1000;

            Statement stmt = ConnObj.createStatement();
            ResultSet rs = stmt.executeQuery(dbType.selectFromTable(tableName));
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            result = new String[MAX_ROWS][rsmd.getColumnCount()];
            int rowNumber = 0;
            while (rs.next() && (rowNumber < MAX_ROWS)) {
                for (int i  = 0 ; i < columnCount; i++ ) {
                    result[rowNumber][i] = rs.getString(i+1);
                }
                rowNumber++;
            }
            String rightRowsResult[][] = new String[rowNumber][columnCount];
            System.arraycopy(result,0,rightRowsResult,0,rowNumber);
            return rightRowsResult;
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return  result;
    }

    private Object[] shrinkArray(Object[] array, int rowsCount){
        Object rightRowsResult[] = new String[rowsCount];
        System.arraycopy(array,0,rightRowsResult,0,rowsCount);
        return rightRowsResult;
    }

    public String[][] tableFind(String tableName){
        if (!tableExist(tableName))
        {
            return null;
        }
        return getRowsFromTable(tableName);
    }
}

