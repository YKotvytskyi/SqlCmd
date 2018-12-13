package ua.com.juja.SqlCmd.model.dbTypes;

public abstract class DBTypeConst {
    final String ServerName;
    final String ServerPort;
    public final String DriverClassName;
    final String DriverStringName;
    final String Schema;


    abstract public String getConnectionString(String database, String username, String password);

    DBTypeConst(String serverName, String serverPort,
                       String driverClassName, String driverStringName, String schema) {
        ServerName = serverName;
        ServerPort = serverPort;
        DriverClassName = driverClassName;
        DriverStringName = driverStringName;
        Schema = schema;
    }

    public String List(){
        return String.format(
                "SELECT Distinct table_name FROM information_schema.TABLES"
                +" WHERE table_schema = '%s'",
                Schema
        );
    }

    public String TableExist(String tableName){
        return String.format(
                "SELECT Distinct table_name FROM information_schema.TABLES "
                        +"where TABLE_SCHEMA = '%s' "
                        +" and TABLE_NAME = '%s' "
                        +"order by 1",
                Schema,
                tableName
                );
    }

    public String Select(String tableName) {
        return  String.format(
                "SELECT * FROM %s.%s order by 1",
                Schema,
                tableName
                );
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
        return String.format("DROP TABLE %s." + tableName,Schema);
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
                "DELETE FROM %s.%s",
                Schema,
                tableName
                );
    }

    public String Update(String[] param){
        String where = param[1] + "= ? ";
        String set = "";
        for (int i = 3; i < param.length; i += 2){
            set += param[i] + "= ?,";
        }
        set = set.substring(0,set.length()-1);

        return String.format("UPDATE %s.%s SET %s WHERE %s",
                Schema,
                param[0],
                set,
                where
                );
    }

    public String Delete(String[] param){
        return String.format("DELETE FROM %s.%s WHERE %s = ?",
                Schema,
                param[0],
                param[1]
        );
    }

}
