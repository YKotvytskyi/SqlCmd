package ua.com.juja.SqlCmd.model;

import sun.security.util.Length;

import java.sql.*;
import java.util.Arrays;

public class Table {

    public Table(String message){
        Error = true;
        Message = message;
    }

    public Table(int value){
        Colums = new String[]{"Result"};
        Rows = new Object[][] {{value}};
    }

    public Table(Object[][] rows,  String[] colums){
        Rows = rows;
        Colums = colums;
    }

    private String Message = "";

    private boolean Error = false;

    public boolean isError() {
        return Error;
    }

    private Object[][] Rows;

    public Object[] getRow(int i){
        return null;
    }

    private String[] Colums;

    public int getLength(){
        return Rows.length;
    }

    @Override
    public String toString() {

        if (Error){
            return Message;
        }

        String result = Arrays.toString(Colums) + "\n";
        result +=  new String(new char[result.length()])
                .replace("\0", "-")+"\n";
        for (Object[] row: Rows ) {
            result += Arrays.toString(row) + "\n";
        }
        return result;
    }
}
