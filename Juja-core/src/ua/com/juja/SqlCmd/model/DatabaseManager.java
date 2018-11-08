package ua.com.juja.SqlCmd.model;

public interface DatabaseManager {

    void Close();

    void setConnection(String database, String username, String password);

    Table Tables();

    Table Clear(String tableName);

    Table Drop(String tableName);

    Table Create(String[] param);

    Table TableExist(String tableName);

    Table Insert(String[] param);

    Table Update(String[] param);

    Table Delete(String[] param);

    Table Find(String tableName);
}
