package ua.com.juja.SqlCmd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.SqlCmd.DdTypes.DBTypeConstMssql;
import ua.com.juja.SqlCmd.DdTypes.DBTypeConstPosgree;


import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {

    private DatabaseManager db;
    private String TableName = "myTable1";

    @Before
    public void setup() {
        db = DatabaseManager.getInstance(new DBTypeConstPosgree());
        db.setConnection("testdb", "postgres", "admin");
    }

    @Test
    public void testGetAllTableNames() {
        assertEquals("[Result]\n" +
                        "---------\n" +
                        "[0]\n",
                db.tableCreate(new String[]{TableName, "r1", "r2"})
                        .toString());
        db.Drop(TableName);
    }

    @Test
    public void testDropTable() {
        db.tableCreate(new String[]{TableName, "r1", "r2"});
        assertEquals("[Result]\n" +
                        "---------\n" +
                        "[0]\n",
                db.Drop(TableName)
                        .toString());
    }

    @Test
    public void testInsertFind() {
        db.tableCreate(new String[]{TableName, "r1", "r2"});
        db.Insert(new String[]{TableName,"r1","0","r2","11 22"});
        assertEquals("[r1, r2]\n" +
                        "---------\n" +
                        "[0, 11 22]\n",
                db.Find(TableName)
                        .toString());
        db.Drop(TableName);
    }

    @After
    public void close(){
        db.Close();
    }
}