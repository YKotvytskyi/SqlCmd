package ua.com.juja.SqlCmd.controller.command;

import ua.com.juja.SqlCmd.view.View;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Help implements Command{

    private View view;
    private String configFile;

    public Help (View view, String configFile){
        this.view = view;
        this.configFile = configFile;
    }

    @Override
    public boolean canProcess(String command) {
        return command.toLowerCase().equals("help");
    }

    @Override
    public void process(String command) {
        try{
            File configPath = new  File(getClass().getClassLoader().getResource(configFile).getFile());
            String helpText = new String(Files.readAllBytes(Paths.get(configPath.getAbsolutePath())));
            view.write(helpText);
        }
        catch (Exception e){
            view.write(String .format("Не могу прочитать файл помощи! '%s'", configFile));
        }
    }
}
