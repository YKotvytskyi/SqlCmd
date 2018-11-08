package ua.com.juja.SqlCmd.controller;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.view.View;

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
            switch (command[0]){
                case "help" : doHelp();
                    break;

                case "list" : doTables();
                    break;

                case "find" : doFind(command);
                    break;

                case "exit" :
                    view.write("До скорой встречи!");
                    System.exit(0);

                default:
                    view.write("Несуществующая команда: " + command);
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
                checkParamLength(cmdParameters.length,3);
                db.setConnection(cmdParameters[0], cmdParameters[0], cmdParameters[0]);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    private void checkParamLength(int paramLength, int properLength){
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

    private void doFind(String[] command) {
        try {
            checkParamLength(command.length,2);
            String tableName = command[1];
            if (db.TableExist(tableName).getLength() > 0) {
                view.write(db.Find(tableName).toString());
            }
            else {
                view.write("Таблицы " + tableName + " не существует");
                view.write(db.Tables().toString());
            }

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
