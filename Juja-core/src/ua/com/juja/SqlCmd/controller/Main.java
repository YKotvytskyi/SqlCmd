package ua.com.juja.SqlCmd.controller;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.JDBCDatabaseManager;
import ua.com.juja.SqlCmd.model.MemDataBaseManager;
import ua.com.juja.SqlCmd.model.dbTypes.DBTypeConstMssql;
import ua.com.juja.SqlCmd.model.dbTypes.DBTypeConstPosgree;
import ua.com.juja.SqlCmd.view.Console;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    public static String configFolderPath = "ua/com/juja/SqlCmd/resource/config/";
    public static String configFileName = "config.properties";

    public static void main(String[] args) {

        Properties prop = getAppProperties();

        String dateSource = prop.getProperty("DateSource");

        MainController controller = new MainController(new Console(),
                getDBManager(dateSource),
                prop
                );

        controller.run();
    }

    private static DatabaseManager getDBManager(String dateSource) {
        DatabaseManager db;

        switch (dateSource){
            case "MSSQL":
                db = JDBCDatabaseManager.getInstance(new DBTypeConstMssql());
                break;
            case "Postgress":
                db = JDBCDatabaseManager.getInstance(new DBTypeConstPosgree());
                break;
            default:
                db = new MemDataBaseManager();
        }

        return db;
    }

    private static Properties getAppProperties() {
        Properties prop = new Properties();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        try {
            prop.load(new FileInputStream(rootPath + configFolderPath + configFileName));
        } catch (Exception e) {
            System.out.printf(
                    String.format( "Файл конфигурации '%s' не найден в каталоге '%s'",
                            configFileName,
                            configFolderPath)
            );
        }
        return prop;
    }

}
