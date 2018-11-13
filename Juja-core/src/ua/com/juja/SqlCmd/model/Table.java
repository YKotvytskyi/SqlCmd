package ua.com.juja.SqlCmd.model;

import java.util.Arrays;

public class Table {

    public Table(String message){
        Error = true;
        this.message = message;
        Columns = new String[0];
        Rows = new Object[0][0];

    }

    public Table(int value){
        Columns = new String[]{"Result"};
        Rows = new Object[][] {{value}};
        setHeadlines();
    }

    public Table(Object[][] rows,  String[] columns){
        Rows = rows;
        this.Columns = columns;
        setHeadlines();
    }

    private String message = "";
    public String getMessage(){
        return message;
    }

    private int[] columnLength;

    private void calculateLength(){
        columnLength = new int[Columns.length];
        int columnsCount = Columns.length;
        for (int i = 0; i < columnsCount; i++){
            columnLength[i] = Columns[i].length();
        }
        for (Object[] row: Rows ) {
            for (int i = 0; i < columnsCount; i++){
                if ( (row[i] != null ) && (columnLength[i] < row[i].toString().length())){
                    columnLength[i] = row[i].toString().length();
                }
            }
        }
    }

    private boolean Error = false;

    public boolean isError() {
        return Error;
    }

    private Object[][] Rows;

    public String PrintRow(int i){
        if (i >= Rows.length) {
            return null;
        }
        Object[] row = Rows[i];
        String  result = "+ ";
        for(int column = 0; column < row.length; column++){
            result +=  getPadRight(row[column], columnLength[column])
                    + " + ";
        }
        return result.substring(0,result.length() - 1);
    }

    private String getPadRight(Object value, int len){
        return String.format("%1$-" + len + "s", value);
    }

    private String columnNames;

    public String getColumnNames(){
        return columnNames;
    }

    private void setHeadlines(){
        calculateLength();
        columnNames = "+ ";
        for(int column = 0; column < Columns.length; column++){
            columnNames += getPadRight(Columns[column], columnLength[column]) + " + ";
        }
        columnNames =  columnNames.substring(0,columnNames.length() - 1);
        horizontalLine = columnNames.replaceAll("[^+]","-");
    }

    private String horizontalLine;

    public String getHorizontalLine(){
        return horizontalLine;
    }

    private String[] Columns;

    public int getLength(){
        return Rows.length;
    }

    @Override
    public String toString() {

        if (Error){
            return message;
        }

        String result = Arrays.toString(Columns) + "\n";
        result +=  new String(new char[result.length()])
                .replace("\0", "-")+"\n";
        for (Object[] row: Rows ) {
            result += Arrays.toString(row) + "\n";
        }
        return result;
    }
}
