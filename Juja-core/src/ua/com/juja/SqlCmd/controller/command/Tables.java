package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.view.View;

public class Tables implements Command {

    private final DatabaseManager dm;
    private final View view;

    public Tables(DatabaseManager dm, View view){
        this.dm = dm;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().equals("tables");
    }

    @Override
    public void process(String command) {
        view.write(dm.Tables().toString());
    }


}
