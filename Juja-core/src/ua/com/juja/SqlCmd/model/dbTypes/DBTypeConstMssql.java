package ua.com.juja.SqlCmd.model.dbTypes;

public final class DBTypeConstMssql extends DBTypeConst {
    public DBTypeConstMssql(){
        super(
                "10.1.32.56",
                "1433",
                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "sqlserver",
                "cmdSQL"
        );
    }

    public String getConnectionString(String database, String username, String password){

        return String.format("jdbc:%1$s://%2$s:%3$s"
                        +";databaseName=%4$s"
                        +";integratedSecurity=true",
                DriverStringName,
                ServerName,
                ServerPort,
                database,
                username,
                password
        );
    }

}