package ua.com.juja.SqlCmd.controller;

import ua.com.juja.SqlCmd.controller.command.*;
import ua.com.juja.SqlCmd.model.*;
import ua.com.juja.SqlCmd.view.View;

import java.io.File;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Properties;

public class MainController {

    public MainController(View view,DatabaseManager dm, Properties properties){
        this.view = view;
        this.dm = dm;
        this.properties = properties;
        String configFile = properties.getProperty("helpFilePath");

        commands = new Command[]{
            new Connect(dm,view),
            new Help(view,configFile),
            new Exit(view)
        };

    }

    private View view;
    private DatabaseManager dm;
    private Properties properties;

    private Command[] commands;

    void run() {
        try {
            doWork();
        } catch (ExitException e) {
            // do nothing
        }
    }

    private void doWork() {
        view.write("Привет юзер!");
        view.write("Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        throw (ExitException)e;
                    }
                    printError(e);
                    break;
                }
            }
            view.write("Введи команду (или help для помощи):");
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

// <editor-fold desc = "Old code">

// <editor-fold desc = "Method 'run'">

/*
    public void run() {

        connectToDb();
        while (true){
            view.write("Введи команду (или help для помощи):");

            String commandLine = view.read();
            String[] command = commandLine.split("\\|");
            String commandName = command[0].toLowerCase();
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
*/

// </editor-fold>

    private void doCreate(String[] command) {
        try {
            checkMinParam(command.length,3);
            Table table = dm.Create(
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
            Table table = dm.Clear(command[1]);
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
                dm.setConnection(cmdParameters[0], cmdParameters[1], cmdParameters[2]);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    private void doDrop(String[] command) {
        try {
            checkExactParam(command.length,2);
            Table table = dm.Drop(command[1]);
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
            if (dm.TableExist(tableName).getLength() > 0) {
                Table table = dm.Find(tableName);
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
                view.write(dm.Tables().toString());
            }
        } catch (Exception e) {
            printError(e);
        }
    }

    private void doInsert(String[] command) {
        try {
            checkMinParam(command.length,3);
            Table table = dm.Insert(
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
            Table table = dm.Update(
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
            Table table = dm.Delete(
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
        view.write(dm.Tables().toString());
    }

    private void doHelp() {
        try{
            String configFile = properties.getProperty("helpFilePath");
            File configPath = new  File(getClass().getClassLoader().getResource(configFile).getFile());
            String helpText = new String(Files.readAllBytes(Paths.get(configPath.getAbsolutePath())));
            view.write(helpText);
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

// </editor-fold>

}

