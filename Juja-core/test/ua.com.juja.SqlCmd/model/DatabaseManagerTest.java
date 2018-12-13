package ua.com.juja.SqlCmd.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.SqlCmd.controller.Main;
import ua.com.juja.SqlCmd.model.*;
import ua.com.juja.SqlCmd.model.dbTypes.*;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {

    private DatabaseManager db;
    private String TableName = "my_table1";

    @Before
    public void setup() {

        String dateSource  = "";

        try {
            Properties prop = new Properties();
            File configFile = new File("src/"
                    + Main.configFolderPath
                    + Main.configFileName);
            prop.load(new FileInputStream(configFile));
            dateSource = prop.getProperty("DateSource","Default");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        switch (dateSource){
            case "MSSQL":
                db = JDBCDatabaseManager.getInstance(new DBTypeConstMssql());
                    db.setConnection("ImportProcessing", "postgres", "admin");
                    break;
            case "Postgress":
                db = JDBCDatabaseManager.getInstance(new DBTypeConstPosgree());
                db.setConnection("testdb", "postgres", "admin");
                break;
            default:
                db = new MemDataBaseManager();
        }

        if (db.TableExist(TableName).getLength() > 0){
            db.Drop(TableName);
        }
        db.Create(new String[]{TableName, "r1", "r2","r3"});
        db.Insert(new String[] {TableName,"r1", "r11_value","r2", "r21_value","r3", "r31_value"});
        db.Insert(new String[] {TableName,"r1", "r12_value","r2", "r22_value","r3", "r32_value"});
    }

    @Test
    public void Create_Insert_GetTableNames() {
        assertEquals("[table_name]\n" +
                        "-------------\n" +
                        "[my_table1]\n",
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
        String expected = "[table_name]\n" +
                "-------------\n" +
                "[my_table1]\n";

        assertEquals(expected,
                db.TableExist(TableName).toString());
        db.Drop(TableName);
        expected ="[table_name]\n" +
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