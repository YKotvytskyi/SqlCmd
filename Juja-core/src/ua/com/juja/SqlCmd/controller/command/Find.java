package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.Table;
import ua.com.juja.SqlCmd.view.View;

import java.util.Arrays;

public class Find implements Command {

    private final DatabaseManager dm;
    private final View view;

    public Find(DatabaseManager dm, View view){
        this.dm = dm;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return (command.toLowerCase().startsWith("find|"));
    }

    @Override
    public void process(String command) {

        String[] cmdData = command.split("\\|");
        if (cmdData.length != 2 ) {
            throw new IllegalArgumentException(
                    String.format(" В команде 'create|TableName' "
                                    +" ожидаеться 2 параметра, разделенных '|', у тебя %s ",
                            cmdData.length)
            );
        }

        String tableName = cmdData[1];
        if (dm.TableExist(tableName).geRowCount() > 0) {
            Table table = dm.Find(tableName);
            if (table.isError()) {
                throw new RuntimeException(table.getMessage());
            }
            view.write(table.getHorizontalLine());
            view.write(table.getColumnNames());
            view.write(table.getHorizontalLine());
            for(int i = 0; i < table.geRowCount(); i++){
                view.write(table.PrintRow(i));
            }
            view.write(table.getHorizontalLine());
        }
        else {
            throw new IllegalArgumentException(
                    String.format("Таблицы '%s' не существует",tableName)
            );
        }
    }
}
