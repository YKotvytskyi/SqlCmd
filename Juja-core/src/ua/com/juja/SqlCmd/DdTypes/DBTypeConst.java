package ua.com.juja.SqlCmd.DdTypes;

public abstract class DBTypeConst {
    public final String ServerName;
    public final String ServerPort;
    public final String DriverClassName;
    public final String DriverStringName;

    abstract public String getConnectionString(String database, String username, String password);

    public DBTypeConst(String serverName, String serverPort,
                       String driverClassName, String driverStringName) {
        ServerName = serverName;
        ServerPort = serverPort;
        DriverClassName = driverClassName;
        DriverStringName = driverStringName;
    }

    public String list(){
        return "SELECT Distinct TABLE_NAME FROM information_schema.TABLES";
    }

    public String checkTableExist(){
        return "SELECT Distinct TABLE_NAME FROM information_schema.TABLES where TABLE_NAME = ?";
    }

    public String selectFromTable(String tableName) {
        return  "SELECT * FROM " + tableName;
    }

}








