package ua.com.juja.SqlCmd.resource.config;

import java.io.*;
import java.util.Properties;

public class GetPropertyValues {
    public static String configFolderPath = "ua/com/juja/SqlCmd/resource/config/";
    public static String configFileName = "config.properties";

    private Properties prop = new Properties();

    public String getProperty(String propertyName){
        return prop.getProperty(propertyName);
    }

    public GetPropertyValues() throws IOException{
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        getValuesFromFile(rootPath);
    }

    private void getValuesFromFile(String rootPath) throws IOException {
        try {
            prop.load(new FileInputStream(rootPath + configFolderPath + configFileName));
        } catch (Exception e) {
            throw new FileNotFoundException(
                    String.format( "property file '%s' not found in the '%s'",
                            configFileName,
                            configFolderPath)
            );
        }
    }
}
