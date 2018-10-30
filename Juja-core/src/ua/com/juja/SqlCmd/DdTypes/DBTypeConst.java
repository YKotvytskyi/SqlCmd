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

    public String Select(){
        return "SELECT Distinct TABLE_NAME FROM information_schema.TABLES";
    }

    public String checkTableExist(){
        return "SELECT Distinct TABLE_NAME FROM information_schema.TABLES where TABLE_NAME = ?";
    }

    public String Select(String tableName) {
        return  "SELECT * FROM " + tableName;
    }

    public String Create(String[] param) {
        String sql = "CREATE TABLE " + param[0] + " (";
        for (int i = 1; i < param.length; i++){
            sql  += " " + param[i] + " nVarChar(150),";
        }
        sql = sql.substring(0,sql.length()-1) + ")";
        return  sql;
    }

    public String Drop(String tableName) {
        return "DROP TABLE " + tableName;
    }

    public String Insert(String[] param){
        String tableName = param[0];
        String fields = "";
        String values = "";
        for (int i = 0; i < (param.length-1)/2; i++ ){
            fields += param[i*2 + 1] + ",";
            values += "?,";
        }
        return String.format("INSERT %1s (%2s) values(%3s)",
                param[0],
                fields.substring(0, fields.length() - 1),
                values.substring(0,values.length() -1)
                );
    }

    public String Clear(String tableName){
        return "DELETE " + tableName;
    }

    public String Update(String[] param){
        return String.format("UPDATE %1s SET %2s = ? WHERE %3s = ?",
                param[0],
                param[3],
                param[1]
                );
    }

    public String Delete(String[] param){
        return String.format("DELETE %1s WHERE %2s = ?",
                param[0],
                param[1]
        );
    }

}

//    public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=tutorialDb;integratedSecurity=true";
//    public static String JDBC_URL = "jdbc:postgresql://localhost:5432;databaseName=testdb?user=postgres&password=admin";







