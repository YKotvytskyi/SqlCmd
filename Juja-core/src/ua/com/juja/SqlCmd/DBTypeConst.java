package ua.com.juja.SqlCmd;

public abstract class DBTypeConst {
    public static String SERVER_NAME;
    public static String SERVER_PORT;

    abstract String getConnectionString(String database, String username, String password);

}

final class DBTypeConstPosgree extends DBTypeConst {
    public static final String SERVER_NAME = "localhost";
    public static final String SERVER_PORT = "5432";

    public String getConnectionString(String database, String username, String password){
        return String.format("jdbc:postgresql://%1$s:%2$s"
                        + "/%3$s"
                        + "?user=%4$s"
                        + "&password=%5$s",

                SERVER_NAME,
                SERVER_PORT,
                database,
                username,
                password
        );
    }
}

final class DBTypeConstMssql extends DBTypeConst {
    public static final String SERVER_NAME = "localhost";
    public static final String SERVER_PORT = "&password";

    public String getConnectionString(String database, String username, String password){

        return String.format("\"jdbc:sqlserver://%1$s:%2$s"
                        +";databaseName=%3$s"
                        +";integratedSecurity=true",
                SERVER_NAME,
                SERVER_PORT,
                database,
                username,
                password
        );
    }
}






