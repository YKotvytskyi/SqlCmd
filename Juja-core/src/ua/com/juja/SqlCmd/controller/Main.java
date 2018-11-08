package ua.com.juja.SqlCmd.controller;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.JDBCDatabaseManager;
import ua.com.juja.SqlCmd.model.dbTypes.DBTypeConstMssql;
import ua.com.juja.SqlCmd.view.Console;

public class Main {
    public static void main(String[] args) {
        MainController controller = new MainController(new Console(),
                JDBCDatabaseManager.getInstance(new DBTypeConstMssql())
                );

        controller.run();
    }
}
