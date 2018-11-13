package ua.com.juja.SqlCmd.controller;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.Table;
import ua.com.juja.SqlCmd.view.View;

import java.util.Arrays;

public class MainController {

    public MainController(View view,DatabaseManager db){
        this.view = view;
        this.db = db;
    }

    View view;
    DatabaseManager db;

    public void run(){
        connectToDb();
        while (true){
            view.write("Введи команду (или help для помощи):");

            String commandLine = view.read();
            String[] command = commandLine.split("\\|");
            String commandName = command[0];
            switch (commandName){
                case "help" : doHelp();
                    break;

                case "list" : doTables();
                    break;

                case "find" : doFind(command);
                    break;

                case "insert" : doInsert(command);
                    break;

                case "exit" :
                    view.write("До скорой встречи!");
                    System.exit(0);

                default:
                    view.write("Несуществующая команда: " + commandName);
            }
        }
    }

    private void connectToDb() {
        view.write("Привет юзер!");
        view.write("Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: database|userName|password");

        while (true){
            try {
                String message = view.read();
                String[] cmdParameters = message.split("\\|");
                checkExactParam(cmdParameters.length,3);
                db.setConnection(cmdParameters[0], cmdParameters[0], cmdParameters[0]);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    private void checkExactParam(int paramLength, int properLength){
        if (paramLength != properLength) {
            throw new RuntimeException(
                    String.format(
                            "Неверно количество параметров разделенных знаком '|', ожидается %s, но есть: %s",
                            properLength,
                            paramLength
                    )
            );
        }
    }

    private void checkMinParam(int paramLength, int minLength){
        if (paramLength < minLength) {
            throw new RuntimeException(
                    String.format(
                            "Неверно количество параметров разделенных знаком '|', ожидается минимум %s, но есть: %s",
                            minLength,
                            paramLength
                    )
            );
        }
    }


    private void doFind(String[] command) {
        try {
            checkExactParam(command.length,2);
            String tableName = command[1];
            if (db.TableExist(tableName).getLength() > 0) {
                Table table = db.Find(tableName);
                view.write(table.getHorizontalLine());
                view.write(table.getColumnNames());
                view.write(table.getHorizontalLine());
                for(int i = 0; i < table.getLength(); i++){
                    view.write(table.PrintRow(i));
                }
                view.write(table.getHorizontalLine());
            }
            else {
                view.write("Таблицы " + tableName + " не существует");
                view.write(db.Tables().toString());
            }

        } catch (Exception e) {
            printError(e);
        }
    }

    private void doInsert(String[] command) {
        try {
            checkMinParam(command.length,3);
            Table table = db.Insert(
                    Arrays.copyOfRange(command,1,command.length));
            if (table.isError()) {
                throw new RuntimeException(table.getMessage());
            }
            view.write(table.getHorizontalLine());
            view.write(table.getColumnNames());
            view.write(table.getHorizontalLine());
            for(int i = 0; i < table.getLength(); i++){
                view.write(table.PrintRow(i));
            }
            view.write(table.getHorizontalLine());

        } catch (Exception e) {
            printError(e);
        }
    }

    private void doTables() {
        view.write(db.Tables().toString());
    }

    private void doHelp() {
        view.write("Существующие команды:");

        view.write("\tlist");
        view.write("\t\tдля получения списка всех таблиц базы, к которой подключились");

        view.write("\tfind|tableName");
        view.write("\t\tдля получения содержимого таблицы 'tableName'");

        view.write("\thelp");
        view.write("\t\tдля вывода этого списка на экран");

        view.write("\texit");
        view.write("\t\tдля выхода из программы");
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + cause.getMessage();
        }
        view.write("Неудача! по причине: " + message);
        view.write("Повтори попытку.");
    }
}
