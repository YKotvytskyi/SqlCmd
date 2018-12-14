package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.view.View;

public class Connect implements Command {

    private static String CONNECT_SAMPLE = "connect|dbName|username|password";

    private DatabaseManager dm;
    private View view;

    public Connect(DatabaseManager dm, View view){

        this.dm = dm;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().startsWith("connect|");
    }

    @Override
    public void process(String command) {
        Utils.checkNumberParameters(CONNECT_SAMPLE, command);
        String[] cmdData = command.split("\\|");
        String dbName = cmdData[1];
        String userName = cmdData[2];
        String password = cmdData[3];
        dm.setConnection(dbName,userName,password);
        view.write("Успех!");
    }
}
