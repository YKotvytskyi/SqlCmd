package ua.com.juja.SqlCmd.model.dbTypes;

public final class DBTypeConstPosgree extends DBTypeConst {

    public DBTypeConstPosgree(){
        super(
                "localhost",
                "5432",
                "org.postgresql.Driver",
                "postgresql",
                "public"
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
