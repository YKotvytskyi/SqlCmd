package ua.com.juja.SqlCmd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.SqlCmd.DdTypes.DBTypeConstMssql;


import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {

    private DatabaseManager db;
    private String TableName = "myTable1";

    @Before
    public void setup() {
//        db = DatabaseManager.getInstance(new DBTypeConstPosgree());
//        db.setConnection("testdb", "postgres", "admin");

        db = DatabaseManager.getInstance(new DBTypeConstMssql());
        db.setConnection("ImportProcessing", "postgres", "admin");
        db.Create(new String[]{TableName, "r1", "r2"});
    }

    @Test
    public void testCreateGetAllTableNames() {
        assertEquals("[TABLE_NAME]\n" +
                        "-------------\n" +
                        "[myTable1]\n",
                db.Tables()
                        .toString());
        db.Drop(TableName);
    }

    @Test
    public void testInsertClearFind(){
        db.Insert(new String[] {TableName,"r1", "r2_value","r2", "r2_value"});
        assertEquals("[r1, r2]\n" +
                "---------\n" +
                "[r2_value, r2_value]\n", db.Find(TableName).toString());
        db.Drop(TableName);
    }

    @Test
    public void testDropTable() {
        assertEquals("[Result]\n" +
                        "---------\n" +
                        "[0]\n",
                db.Drop(TableName)
                        .toString());
    }

    @After
    public void close(){
        db.Close();
    }
}