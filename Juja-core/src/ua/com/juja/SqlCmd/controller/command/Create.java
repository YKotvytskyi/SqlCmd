package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.Table;
import ua.com.juja.SqlCmd.view.View;

import java.util.Arrays;

public class Create implements Command {

    private View view;
    private DatabaseManager dm;

    public Create(DatabaseManager dm, View view){
        this.view = view;
        this.dm = dm;
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().startsWith("create|");
    }

    @Override
    public void process(String command)  {
        String[] cmdData = command.split("\\|");
        if (cmdData.length < 2 ) {
            throw new IllegalArgumentException(
                    String.format(" В команде 'create|TableName' "
                            +" ожидаеться не менее 2 параметров, разделенных '|', у тебя %s ",
                            cmdData.length)
            );
        }

        Table table = dm.Create(
                Arrays.copyOfRange(cmdData,1,cmdData.length));
        if (table.isError()) {
            throw new RuntimeException(table.getMessage());
        }
        view.write(String.format("Таблица была '%s' успещно создана",cmdData[1]));
    }
}
