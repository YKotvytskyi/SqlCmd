package ua.com.juja.SqlCmd.controller.Command;

import ua.com.juja.SqlCmd.view.View;

public class FakeView implements View {

    private String written = "";

    @Override
    public String read() {
        return null;
    }

    @Override
    public void write(String message) {
        written += message + "\n";
    }

    public String getWritten() {
        return written;
    }
}
