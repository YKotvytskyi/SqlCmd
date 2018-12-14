package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.view.View;

public class Unsupported implements Command{

    private View view;

    public Unsupported(View view){
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) throws Exception {
        view.write("Несуществующая команда: " + command);
    }

}
