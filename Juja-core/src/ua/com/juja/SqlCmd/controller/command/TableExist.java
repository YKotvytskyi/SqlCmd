package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.Table;
import ua.com.juja.SqlCmd.view.View;

public class TableExist implements Command {

    private final DatabaseManager dm;
    private final View view;

    public TableExist(DatabaseManager dm, View view){
        this.dm = dm;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().startsWith("tableexist|");
    }

    @Override
    public void process(String command){
        String[] cmdData = command.split("\\|");
        if (cmdData.length != 2) {
            throw new IllegalArgumentException(
                    String.format(
                            "В комаде 'TableExist|TableName' должно быть два параметра, "
                            +"разделенных '|', у тебя %s",
                            cmdData.length)
            );
        }
        Table table = dm.TableExist(cmdData[1]);
        if (table.isError()) {
            throw new RuntimeException(table.getMessage());
        }
        if (table.geRowCount() > 0){
            view.write(String.format("Таблица '%s' существует.",cmdData[1]));
        }
        else {
            view.write(String.format("Таблица '%s' не существует.",cmdData[1]));
        }
    }
}
