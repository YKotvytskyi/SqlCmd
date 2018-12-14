package ua.com.juja.SqlCmd.controller.command;

public class Utils {

    static void checkNumberParameters(String sample, String commandLine){
        String[] cmdData = commandLine.split("\\|");
        int cmdParamCount = sample.split("\\|").length;
        if (cmdData.length != cmdParamCount) {
            throw new IllegalArgumentException(
                    String.format("Неверно количество параметров разделенных "
                                    +"знаком '|', ожидается %s, но есть: %s",
                            cmdParamCount,
                            cmdData.length
                    )
            );
        }
    }

    static void chechEvenParameters(String sample){
        String[] cmdData = sample.split("\\|");
        if ((cmdData.length & 1) != 0) {
            throw new IllegalArgumentException(
                    String.format("Неверно количество параметров разделенных "
                            +"знаком '|', нужно четное количество, введено %s",
                            cmdData.length
                    )
            );
        }
    }
}
