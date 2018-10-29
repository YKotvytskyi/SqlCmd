package ua.com.juja.SqlCmd;

public abstract class DBTypeConst {
    public final String ServerName;
    public final String ServerPort;
    public final String DriverClassName;
    public final String DriverStringName;

    abstract String getConnectionString(String database, String username, String password);

    public DBTypeConst(String serverName, String serverPort,
                       String driverClassName, String driverStringName) {
        ServerName = serverName;
        ServerPort = serverPort;
        DriverClassName = driverClassName;
        DriverStringName = driverStringName;
    }


}

final class DBTypeConstPosgree extends DBTypeConst {

    public DBTypeConstPosgree(){
        super(
        "localhost",
        "5432",
        "org.postgresql.Driver",
        "postgresql"
                );
    }

    public String getConnectionString(String database, String username, String password){
        return String.format("jdbc:%1$s://%2$s:%3$s"
                        + "/%4$s"
                        + "?user=%5$s"
                        + "&password=%6$s",
                DriverStringName,
                ServerName,
                ServerPort,
                database,
                username,
                password
        );
    }
}

final class DBTypeConstMssql extends DBTypeConst {
    public DBTypeConstMssql(){
        super(
            "sqlserver",
            "10.1.32.56",
            "1433",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver"
        );
    }

    public String getConnectionString(String database, String username, String password){

        return String.format("jdbc:%1$s://%2$s:%3$s"
                        +";databaseName=%4$s"
                        +";integratedSecurity=true",
                DriverClassName,
                ServerName,
                ServerPort,
                database,
                username,
                password
        );
    }
}






