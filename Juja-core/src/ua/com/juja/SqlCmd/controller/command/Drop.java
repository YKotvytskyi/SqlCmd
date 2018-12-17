package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.Table;
import ua.com.juja.SqlCmd.view.View;

public class Drop implements Command {

    private final DatabaseManager dm;
    private final View view;

    public Drop(DatabaseManager dm, View view){
        this.dm = dm;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().startsWith("drop|");
    }

    @Override
    public void process(String command) {
        String[] cmdData = command.split("\\|");
        if (cmdData.length != 2){
            throw new IllegalArgumentException(
                    String.format(" В команде 'create|Drop' "
                                    +" ожидаеться не менее 2 параметров, разделенных '|', у тебя %s ",
                            cmdData.length)

            );
        }
        String tableName = cmdData[1];
        Table table = dm.Drop(tableName);
        if (table.isError()){
            view.write(table.getMessage());
        }
        else {
            view.write(String.format("Таблица %s успешно удалена!",
                    tableName));
        }
    }
}
