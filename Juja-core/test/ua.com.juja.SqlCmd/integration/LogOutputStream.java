package ua.com.juja.SqlCmd.integration;

import java.io.IOException;
import java.io.OutputStream;

public class LogOutputStream extends OutputStream {

    private String output;

    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[] { (byte)(b & 0xFF00 >> 8), (byte)(b & 0x00FF) };
        output += new String(bytes, "UTF-8");
    }

    public String getData() {
        return output;
    }
}
