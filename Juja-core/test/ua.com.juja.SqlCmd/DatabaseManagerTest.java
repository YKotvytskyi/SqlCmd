package ua.com.juja.SqlCmd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.JDBCDatabaseManager;
import ua.com.juja.SqlCmd.model.dbTypes.DBTypeConstMssql;


import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {

    private DatabaseManager db;
    private String TableName = "myTable1";

    @Before
    public void setup() {
//        db = JDBCDatabaseManager.getInstance(new DBTypeConstPosgree());
//        db.setConnection("testdb", "postgres", "admin");

        db = JDBCDatabaseManager.getInstance(new DBTypeConstMssql());
        db.setConnection("ImportProcessing", "postgres", "admin");
        if (db.TableExist(TableName).getLength() > 0){
            db.Drop(TableName);
        }
        db.Create(new String[]{TableName, "r1", "r2","r3"});
        db.Insert(new String[] {TableName,"r1", "r11_value","r2", "r21_value","r3", "r31_value"});
        db.Insert(new String[] {TableName,"r1", "r12_value","r2", "r22_value","r3", "r32_value"});
    }

    @Test
    public void Create_Insert_GetTableNames() {
        assertEquals("[TABLE_NAME]\n" +
                        "-------------\n" +
                        "[myTable1]\n",
                db.Tables()
                        .toString());
        db.Drop(TableName);
    }

    @Test
    public void Insert_Find_Clear(){
        db.Clear(TableName);
        db.Insert(new String[] {TableName,"r1", "r11_value"});
        db.Insert(new String[] {TableName,"r1", "r11_value","r2", "r21_value"});
        String expected = "[r1, r2, r3]\n" +
                "-------------\n" +
                "[r11_value, null, null]\n" +
                "[r11_value, r21_value, null]\n";
        assertEquals(expected, db.Find(TableName).toString());
        db.Drop(TableName);
    }

    @Test
    public void ExistTable_Drop(){
        String expected = "[TABLE_NAME]\n" +
                "-------------\n" +
                "[myTable1]\n";

        assertEquals(expected,
                db.TableExist(TableName).toString());
        db.Drop(TableName);
        expected ="[TABLE_NAME]\n" +
                "-------------\n";
        assertEquals(expected,
                db.TableExist(TableName).toString());
    }

    @Test
    public void Insert_Delete(){
        db.Delete(new String[] {TableName,"r1", "r12_value"});
        String expected = "[r1, r2, r3]\n" +
                "-------------\n" +
                "[r11_value, r21_value, r31_value]\n";
        assertEquals(expected, db.Find(TableName).toString());
        db.Drop(TableName);
    }

    @Test
    public void Update_Find() {
        String expected = "[r1, r2, r3]\n" +
                "-------------\n" +
                "[r11_value, r21_value, r31_value]\n" +
                "[r12_value, r22_value, r32_value]\n";
        assertEquals(expected,db.Find(TableName).toString());

        assertEquals("[Result]\n" +
                        "---------\n" +
                        "[1]\n",
                db.Update(new String[]
                        {TableName,
                                "r1", "r11_value",
                                "r2", "r12_new_value",
                                "r3", "r13_new_value"
                        }).toString()
        );
        expected = "[r1, r2, r3]\n" +
                "-------------\n" +
                "[r11_value, r12_new_value, r13_new_value]\n" +
                "[r12_value, r22_value, r32_value]\n";
        assertEquals(expected,db.Find(TableName).toString());
    }

    @Test
    public void Delete_Find() {
        String expected = "[r1, r2, r3]\n" +
                "-------------\n" +
                "[r11_value, r21_value, r31_value]\n" +
                "[r12_value, r22_value, r32_value]\n";
        assertEquals(expected,db.Find(TableName).toString());

        db.Delete(new String[] {TableName,"r1", "r12_value"});
        expected = "[r1, r2, r3]\n" +
                "-------------\n" +
                "[r11_value, r21_value, r31_value]\n";
        assertEquals(expected,db.Find(TableName).toString());
    }

    @After
    public void close(){
        db.Close();
    }
}