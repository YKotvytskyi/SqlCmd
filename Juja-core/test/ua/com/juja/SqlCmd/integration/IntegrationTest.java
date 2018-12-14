package ua.com.juja.SqlCmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.SqlCmd.controller.Main;

import javax.imageio.IIOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in = new ConfigurableInputStream();
    private ByteArrayOutputStream out = new ByteArrayOutputStream();

// <editor-fold desc = "App message strings">

    private String appHelloMessage = "Привет юзер!\r\n" +
            "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате:" +
            " connect|database|userName|password\r\n";

    private String appExitMessage = "Введи команду (или help для помощи):\r\n" +
            "До скорой встречи!\r\n";

//    private String appNewCommand = "Введи команду (или help для помощи):\r\n";

// </editor-fold>

    @Before
    public void setup(){
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testHelp(){
        in.add("help");
        in.add("exit");
        Properties prop = getAppProperties();
        Main.main(new String[0]);
        String appAnswer = getData();
        String appHelpScreen = getHelpText(prop.getProperty("helpFilePath"));
        assertEquals(appHelloMessage + appHelpScreen + "\r\n" + appExitMessage,
                appAnswer);
    }

    @Test
    public void testConnect(){

        in.add("connect|ImportProcessing|user|password");
        in.add("exit");
        Main.main(new String[0]);
        String appAnswer = getData();
        assertEquals(appHelloMessage
                        + "Успех!" + "\r\n"
                        + appExitMessage ,
                appAnswer);

        try{
            in.reset();
        }
        catch (IOException e){
            System.out.printf(e.getMessage());
        }

        in.add("connect|ImportProcessing|user");
        in.add("exit");
        getData();
        Main.main(new String[0]);
        appAnswer = getData();
        assertEquals(appHelloMessage
                        + "Неудача! по причине: Неверно количество параметров разделенных знаком '|', ожидается 4, но есть: 3\r\n" +
                        "Повтори попытку." + "\r\n"
                        + appExitMessage ,
                appAnswer);
    }

    @Test
    public void testUnsupported(){
        in.add("unsupported");
        in.add("exit");
        Main.main(new String[0]);
        String appAnswer = getData();
        assertEquals(appHelloMessage
                        + "Несуществующая команда: unsupported" + "\r\n"
                        + appExitMessage ,
                appAnswer);
    }

    @Test
    public void testCreate(){
        in.add("connect|ImportProcessing|user|password");
        in.add("create|my_table1|col1|col2");
        in.add("exit");
        Main.main(new String[0]);
        String appAnswer = getData();
        assertEquals(appHelloMessage
                        + "Успех!" + "\r\n"
                        + appExitMessage ,
                appAnswer);

    }

    Properties getAppProperties() {
        String configFolderPath = "ua/com/juja/SqlCmd/resource/config/";
        String configFileName = "config.properties";

        Properties prop = new Properties();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        try {
            prop.load(new FileInputStream(rootPath + configFolderPath + configFileName));
        } catch (Exception e) {
            System.out.printf(
                    String.format( "Файл конфигурации '%s' не найден в каталоге '%s'",
                            configFileName,
                            configFolderPath)
            );
        }
        return prop;
    }

    String getHelpText(String configFile){
        String helpText = null;
        try{
            File configPath = new  File(getClass().getClassLoader().getResource(configFile).getFile());
            helpText = new String(Files.readAllBytes(Paths.get(configPath.getAbsolutePath())));
        }
        catch (IOException e){
            System.out.printf(
                    String.format( "Файл с текстом помощи '%s' не найден",
                            configFile)
            );
        }
        return helpText;
    }

}
