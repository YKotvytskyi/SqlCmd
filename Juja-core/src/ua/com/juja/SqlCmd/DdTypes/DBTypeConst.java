package ua.com.juja.SqlCmd.DdTypes;

import org.omg.PortableServer.ServantRetentionPolicyValue;

public abstract class DBTypeConst {
    public final String ServerName;
    public final String ServerPort;
    public final String DriverClassName;
    public final String DriverStringName;
    public final String Schema;


    abstract public String getConnectionString(String database, String username, String password);

    public DBTypeConst(String serverName, String serverPort,
                       String driverClassName, String driverStringName, String schema) {
        ServerName = serverName;
        ServerPort = serverPort;
        DriverClassName = driverClassName;
        DriverStringName = driverStringName;
        Schema = schema;
    }

    public String Select(){
        return String.format(
                "SELECT Distinct TABLE_NAME FROM information_schema.TABLES"
                +" WHERE table_schema = '%s'",
                Schema
        );
    }

    public String checkTableExist(){
        return String.format(
                "SELECT Distinct TABLE_NAME FROM information_schema.TABLES where TABLE_NAME = %s.?",
                Schema
                );
    }

    public String Select(String tableName) {
        return  "SELECT * FROM " + tableName;
    }

    public String Create(String[] param) {
        String sql = String.format(
                "CREATE TABLE %s." + param[0] + " (",
                Schema
            );
        for (int i = 1; i < param.length; i++){
            sql  += " " + param[i] + " VarChar(150),";
        }
        sql = sql.substring(0,sql.length()-1) + ")";
        return  sql;
    }

    public String Drop(String tableName) {
        return "DROP TABLE " + tableName;
    }

    public String Insert(String[] param){
        String fields = "";
        String values = "";
        for (int i = 0; i < (param.length-1)/2; i++ ){
            fields += param[i*2 + 1] + ",";
            values += "?,";
        }
        return String.format("INSERT INTO %s.%s (%s) values(%s)",
                Schema,
                param[0],
                fields.substring(0, fields.length() - 1),
                values.substring(0,values.length() -1)
                );
    }

    public String Clear(String tableName){
        return String.format(
                "DELETE %s.%s",
                Schema,
                tableName
                );
    }

    public String Update(String[] param){
        return String.format("UPDATE %s.%s SET %s = ? WHERE %s = ?",
                Schema,
                param[0],
                param[3],
                param[1]
                );
    }

    public String Delete(String[] param){
        return String.format("DELETE %s.%s WHERE %s = ?",
                Schema,
                param[0],
                param[1]
        );
    }

}

//    public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=tutorialDb;integratedSecurity=true";
//    public static String JDBC_URL = "jdbc:postgresql://localhost:5432;databaseName=testdb?user=postgres&password=admin";







