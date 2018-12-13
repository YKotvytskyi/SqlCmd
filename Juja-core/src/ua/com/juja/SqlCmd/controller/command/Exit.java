package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.view.View;

public class Exit implements Command {
    private View view;

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("До скорой встречи!");
        throw new ExitException();
    }
}
