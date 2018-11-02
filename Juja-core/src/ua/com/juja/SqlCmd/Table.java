package ua.com.juja.SqlCmd;

import java.sql.*;
import java.util.Arrays;

public class Table {

    public Table(){
        Colums = new String [0];
        Rows = new Object[0][0];
    }

    public Table(int value){
        Colums = new String[]{"Result"};
        Rows = new Object[][] {{value}};
    }

    public Table(Object[][] rows,  String[] colums){
        Rows = rows;
        Colums = colums;
    }

    Object[][] Rows;

    public Object[] getRow(int i){
        return null;
    }

    String[] Colums;

    @Override
    public String toString() {

        String result = Arrays.toString(Colums) + "\n";
        result +=  new String(new char[result.length()])
                .replace("\0", "-")+"\n";
        for (Object[] row: Rows ) {
            result += Arrays.toString(row) + "\n";
        }
        return result;
    }
}
