package ua.com.juja.SqlCmd.controller;

import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.Table;
import ua.com.juja.SqlCmd.view.View;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class MainController {

    public MainController(View view,DatabaseManager db){
        this.view = view;
        this.db = db;
    }

    View view;
    DatabaseManager db;

    public void run() {

        connectToDb();
        while (true){
            view.write("Введи команду (или help для помощи):");

            String commandLine = view.read();
            String[] command = commandLine.split("\\|");
            String commandName = command[0];
            switch (commandName){
                case "help"     : doHelp();
                    break;

                case "list"     : doTables();
                    break;

                case "clear"    : doClear(command);
                    break;

                case "drop"     : doDrop(command);
                    break;

                case "create"   : doCreate(command);
                    break;

                case "find"     : doFind(command);
                    break;

                case "insert"   : doInsert(command);
                    break;

                case "update"   : doUpdate(command);
                    break;

                case "delete"   : doDelete(command);
                    break;

                case "exit" :
                    view.write("До скорой встречи!");
                    System.exit(0);

                default:
                    view.write("Несуществующая команда: " + commandName);
            }
        }
    }

    private void doCreate(String[] command) {
        try {
            checkMinParam(command.length,3);
            Table table = db.Create(
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

    private void doClear(String[] command) {
        try {
            checkExactParam(command.length,2);
            Table table = db.Clear(command[1]);
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

    private void doDrop(String[] command) {
        try {
            checkExactParam(command.length,2);
            Table table = db.Drop(command[1]);
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

    private void doUpdate(String[] command) {
        try {
            checkMinParam(command.length,6);
            Table table = db.Update(
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

    private void doDelete(String[] command) {
        try {
            checkExactParam(command.length,4);
            Table table = db.Delete(
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
        try{
            Path path = FileSystems.getDefault().getPath(
                    "juja-core","src","ua","com","juja","SqlCmd","help","help.txt");
            view.write(new String(Files.readAllBytes(path)));
        }
        catch (Exception e){
            view.write("Не могу прочитать файл помощи!");
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
