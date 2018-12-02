package ua.com.juja.SqlCmd.model;

import ua.com.juja.SqlCmd.model.dbTypes.DBTypeConst;
import ua.com.juja.SqlCmd.model.dbTypes.DBTypeConstPosgree;

import java.sql.*;


public class JDBCDatabaseManager implements DatabaseManager {

    private static JDBCDatabaseManager instance = new JDBCDatabaseManager();

    static DBTypeConst dbType = new DBTypeConstPosgree();

    private JDBCDatabaseManager(){}

    public void setDbType(DBTypeConst dbType){
        if (this.dbType.getClass() != dbType.getClass()){
            ConnObj = null;
            this.dbType = dbType;
        }
    }

    public static Connection ConnObj;

    public static JDBCDatabaseManager getInstance(DBTypeConst dbType){
        instance.setDbType(dbType);
        return instance;
    }

    @Override
    public void Close(){
        try{
            if(ConnObj != null) {
                ConnObj.close();
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void setConnection(String database, String username, String password) {
        String jdbc_url = getConnectionString(database,username,password);
        try {
            Class.forName(dbType.DriverClassName);
            ConnObj = DriverManager.getConnection(jdbc_url);
        } catch(Exception sqlException) {
            System.out.printf(
                    "Не могу загрузить драйвер доступа к базе данных!\n%s",
                    sqlException.getMessage()
            );
        }
    }

    private String getConnectionString(String database, String username, String password){
        return dbType.getConnectionString(database, username, password);
    }

    @Override
    public Table Tables(){
        return executeQuery(dbCommand.Tables);
    }

    @Override
    public Table Clear(String tableName){
        return executeUpdate(dbCommand.Clear, new Object[] {tableName});
    }

    @Override
    public Table Drop(String tableName){
        return executeUpdate(dbCommand.Drop, new String[] {tableName});
    }

    @Override
    public Table Create(String[] param){
        return executeUpdate(dbCommand.Create, param);
    }

    @Override
    public Table TableExist(String tableName){
        return  executeQuery(dbCommand.TableExist, new Object[] {tableName});
    }

    @Override
    public Table Insert(String[] param){
        return executeUpdate(dbCommand.Insert, param);
    }

    @Override
    public Table Update(String[] param){
        return executeUpdate(dbCommand.Update, param);
    }

    @Override
    public Table Delete(String[] param){
        return executeUpdate(dbCommand.Delete, param);
    }

    @Override
    public Table Find(String tableName){
        return  executeQuery(dbCommand.Find, new Object[] {tableName});
    }

    private Table executeQuery(dbCommand sqlCmdType, Object[] param ){
        try {
            PreparedStatement stmt = null;
            switch (sqlCmdType) {
                case Tables:
                    stmt = ConnObj.prepareStatement(dbType.List());
                    break;
                case Find :
                    stmt = ConnObj.prepareStatement(dbType.Select(param[0].toString()));
                    break;
                case TableExist:
                    stmt = ConnObj.prepareStatement(dbType.TableExist(param[0].toString()));
                    break;
            }
            getDataFromDB(stmt.executeQuery(), stmt.getMetaData());
            return new Table(Rows, Columns);
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
            return new Table(sqlException.getMessage());
        }
    }

    private Table executeQuery(dbCommand sqlCmdType){
        return executeQuery(sqlCmdType, null);
    }

    private Table executeUpdate(dbCommand sqlCmdType, Object[] param ){

        try {
            PreparedStatement stmt = null;
            switch (sqlCmdType) {
                case Create:
                    stmt = ConnObj.prepareStatement(dbType.Create((String[]) param));
                    break;
                case Insert:
                    stmt = ConnObj.prepareStatement(dbType.Insert((String[]) param));
                    for (int i = 0; i < param.length/2; i++){
                        stmt.setString(i + 1, param[i*2 + 2].toString());
                    }
                    break;
                case Drop:
                    stmt = ConnObj.prepareStatement(dbType.Drop(param[0].toString()));
                    break;
                case Clear:
                    stmt = ConnObj.prepareStatement(dbType.Clear(param[0].toString()));
                    break;
                case Update:
                    stmt = ConnObj.prepareStatement(dbType.Update((String[]) param));
                    int lastParamIndex = param.length  / 2;
                    stmt.setString(lastParamIndex, param[2].toString());
                    for (int i = 1; i < lastParamIndex; i++ ) {
                        stmt.setString(i, param[ (i + 1) * 2 ].toString());
                    }
                    //stmt.setString(2, param[2].toString());
                    break;
                case Delete:
                    stmt = ConnObj.prepareStatement(dbType.Delete((String[]) param));
                    stmt.setString(1, param[2].toString());
            }
            return new Table(stmt.executeUpdate());
        } catch(Exception sqlException) {
            //sqlException.printStackTrace();
            return new Table(String.format("%s (%s)",
                    sqlException.getMessage(),
                    sqlException.getClass().getSimpleName()));
        }
    }

    private String[] Columns;
    private Object[][] Rows;

    private void getDataFromDB(ResultSet rs, ResultSetMetaData rsmd){
        final int maxRow = 10000;
        try{

            int columnCount = rsmd.getColumnCount();

            Columns = new String[rsmd.getColumnCount()];
            for(int i = 0; i < rsmd.getColumnCount(); i++){
                Columns[i] = rsmd.getColumnName(i+1);
            }

            Object[][] rows = new Object[maxRow][columnCount];
            int rowNumber = 0;
            while (rs.next() && (rowNumber < maxRow)) {
                for (int i  = 0 ; i < columnCount; i++ ) {
                    rows[rowNumber][i] = rs.getString(i+1);
                }
                rowNumber++;
            }
            Rows = new Object[rowNumber][columnCount];

            System.arraycopy(rows,0,Rows,0, rowNumber);
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    enum dbCommand {
        Find, Tables,
        Create, Drop, Insert, Clear, Update, Delete,
        TableExist
    }

}

