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

    public Object[][] Tables(){
        return executeQuery(dbCommand.Tables);
    }

    public Object[][] create(){
        return executeQuery(dbCommand.Tables);
    }

    public Object[][] drop(){
        return executeQuery(dbCommand.Drop);
    }

    public Object[][] tableFind(String tableName){
        if (!tableExist(tableName))
        {
            return null;
        }
        return getRowsFromTable(tableName);
    }

    public Object[][] tableCreate(String[] param){
        return executeQuery(dbCommand.Create, param);
    }

    public Object[][] Drop(String tableName){
        return executeQuery(dbCommand.Drop, new Object[]{tableName});
    }

    public Object[][] Insert(String[] param){
        return executeQuery(dbCommand.Insert, param);
    }

    public Object[][] Clear(String tableName){
        return executeQuery(dbCommand.Clear, new Object[] {tableName});
    }

    public Object[][] Update(String[] param){
        return executeQuery(dbCommand.Update, param);
    }

    public Object[][] Delete(String[] param){
        return executeQuery(dbCommand.Delete, param);
    }

    private boolean tableExist(String tableName){
        Object[][] result = executeQuery(dbCommand.Table_Exist,new Object[] {tableName});
        if (result.length > 0 ){
            return true;
        }
        return false;
    }

    private Object[][] getRowsFromTable(String tableName){
        Object[][] result = null;
        return  executeQuery(dbCommand.Find, new Object[] {tableName});
    }

    private Object[][] shrinkArray(Object[][] array, int rowsCount){
        if (array.length == 0)
            return  array;
        Object rightRowsResult[][] = new Object[rowsCount][array[0].length];
        System.arraycopy(array,0,rightRowsResult,0,rowsCount);
        return rightRowsResult;
    }

    private Object[][] executeQuery(dbCommand sqlCmdType,Object[] param ){

        Object[][] result = null;
        try {
            PreparedStatement stmt = null;
            switch (sqlCmdType) {
                case Tables:
                    stmt = ConnObj.prepareStatement(dbType.Select());
                    break;
                case Find :
                    stmt = ConnObj.prepareStatement(dbType.Select(param[0].toString()));
                    break;
                case Table_Exist :
                    stmt = ConnObj.prepareStatement(dbType.checkTableExist());
                    stmt.setString(1, param[0].toString());
                    break;
                case Create:
                    String[] createParam = new String[param.length -1 ];
                    System.arraycopy(param, 1, createParam,0,param.length - 1);
                    stmt = ConnObj.prepareStatement(dbType.Create((String[]) param));
                    return new Object[][] {{stmt.executeUpdate() }};
                case Insert:
                    stmt = ConnObj.prepareStatement(dbType.Insert((String[]) param));
                    for (int i = 0; i < param.length/2; i++){
                        stmt.setString(i + 1, param[i*2 + 2].toString());
                    }
                    return value2Array(stmt.executeUpdate());
                case Drop:
                    stmt = ConnObj.prepareStatement(dbType.Drop(param[0].toString()));
                    return value2Array(stmt.executeUpdate());
                case Clear:
                    stmt = ConnObj.prepareStatement(dbType.Clear(param[0].toString()));
                    return value2Array(stmt.executeUpdate());
                case Update:
                    stmt = ConnObj.prepareStatement(dbType.Update((String[]) param));
                    stmt.setString(1, param[4].toString());
                    stmt.setString(2, param[2].toString());
                    return value2Array(stmt.executeUpdate());
                case Delete:
                    stmt = ConnObj.prepareStatement(dbType.Delete((String[]) param));
                    stmt.setString(1, param[2].toString());
                    return value2Array(stmt.executeUpdate());
            }
            return recordsToArray(stmt.executeQuery());
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return result;
    }

    private Object[][] value2Array(Object val){
        return new Object[][] {{val}};
    }

    private Object[][] executeQuery(dbCommand sqlCmdType){
        return executeQuery(sqlCmdType, null);
    }

    private Object[][] recordsToArray(ResultSet rs){
        final int maxRow = 1000;
        Object[][] result = null;
        int rowNumber = 0;
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            result = new Object[maxRow][rsmd.getColumnCount()];
            while (rs.next() && (rowNumber < maxRow)) {
                for (int i  = 0 ; i < columnCount; i++ ) {
                    result[rowNumber][i] = rs.getString(i+1);
                }
                rowNumber++;
            }
        } catch(Exception sqlException) {
        sqlException.printStackTrace();
        }
        return shrinkArray(result,rowNumber);
    }

    enum dbCommand {
        Find, Table_Exist, Tables,
        Create, Drop, Insert, Clear, Update, Delete

    }

}

