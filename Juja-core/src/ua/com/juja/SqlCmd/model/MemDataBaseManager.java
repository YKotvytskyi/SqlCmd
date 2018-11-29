package ua.com.juja.SqlCmd.model;

import java.util.Arrays;

public class MemDataBaseManager implements DatabaseManager {

    private String tableName;
    private String[] tableTitle;
    private String[][] tableRows;

    @Override
    public void Close() {
    }

    @Override
    public void setConnection(String database, String username, String password) {
    }

    @Override
    public Table Tables() {
        if (tableName != null) {
            return new Table(new String[][]{{tableName}}, new String[]{"table_name"});
        }
        return new Table(new String[0][], new String[]{"table_name"});
    }

    @Override
    public Table Clear(String tableName) {
        if (this.tableName.equals(tableName) ){
            tableRows = new String[0][];
            return new  Table(1);
        }
        return new Table(0);
    }

    @Override
    public Table Drop(String tableName) {
        if(this.tableName.equals(tableName)) {
            this.tableName = null;
            tableTitle = null;
            tableRows = null;
            return new Table(1);
        }
        return new Table(0);
    }

    @Override
    public Table Create(String[] param) {

        tableName = param[0];
        tableTitle = Arrays.copyOfRange(param, 1, param.length );

        tableRows = new String[0][];

        return new Table(
                new String[][] {{"table_name"}},
                new String[] {"my_table1"}
        );
    }

    @Override
    public Table TableExist(String tableName) {
        if ((this.tableName != null) && (this.tableName.equals(tableName))){
            return new Table(new String[][]{{tableName}},new String[]{"table_name"});
        }
        return new Table(new String[0][], new String[]{"table_name"});
    }

    @Override
    public Table Insert(String[] param) {
        String[][] newRows = new String[tableRows.length + 1][tableTitle.length];
        System.arraycopy(tableRows,0, newRows, 0 , tableRows.length);
        for (int i = 1 ; i < param.length ; i+=2 ){
            newRows[tableRows.length ][getColumnNumber(param[i])]
                    = param[i+1];
        }
        tableRows = newRows;
        return new Table(1);
    }

    @Override
    public Table Update(String[] param) {
        int colCondionNumber = getColumnNumber(param[1]);
        if (colCondionNumber == -1) {
            return new Table(0);
        }

        int colRowsToUpdate = getColRowsToModify(param[2], colCondionNumber);

        if (colRowsToUpdate == 0 ){
            return new Table(0);
        }

        class Value {
            private int index;
            private String value;

            private Value(String index, String value){
                this.index = getColumnNumber(index);
                this.value = value;
            }

        }

        Value[] values = new Value [(param.length - 3) / 2];

        for (int i = 0; i < values.length; i++) {
            int paramIndex = 3 + i * 2;
            values[i] = new Value(param[paramIndex], param[paramIndex + 1]);
        }

        for (int i = 0; i < tableRows.length; i++) {
            for (Value value: values) {
                if ((value.index != -1) && (tableRows[i][colCondionNumber].equals(param[2]))){
                    tableRows[i][value.index] = value.value;
                }
            }
        }
        return new Table(colRowsToUpdate);
    }

    @Override
    public Table Delete(String[] param) {
        int colNumber = getColumnNumber(param[1]);
        if (colNumber == -1) {
            return new Table(0);
        }
        int colRowsToDelete = getColRowsToModify(param[2], colNumber);

        if (colRowsToDelete == 0 ){
            return new Table(0);
        }
        String[][] newRows = new String[tableRows.length - colRowsToDelete][tableTitle.length];
        int newRowNumber = 0;
        for (int i = 0; i < tableRows.length; i++){
            if (!tableRows[i][colNumber].equals(param[2])) {
                newRows[newRowNumber] = tableRows[i];
                i++;
            }
        }
        tableRows = newRows;
        return new Table(colRowsToDelete);
    }

    private int getColRowsToModify(String s, int colNumber) {
        int colRowsToDelete = 0;
        for (String[] row: tableRows) {
            if (row[colNumber].equals(s)){
                colRowsToDelete++;
            }
        }
        return colRowsToDelete;
    }

    @Override
    public Table Find(String tableName) {
        return new Table(tableRows, tableTitle);
    }

    private int getColumnNumber(String name){
        for(int i = 0; i < tableTitle.length; i++){
            if (tableTitle[i].equals(name)){
                return i;
            }
        }
        return -1;
    }

}
