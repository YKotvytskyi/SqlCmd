package ua.com.juja.SqlCmd;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.SqlCmd.DdTypes.DBTypeConstMssql;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {

    private DatabaseManager db;

    @Before
    public void setup(){
        db = DatabaseManager.getInstance(new DBTypeConstMssql());
    }

    @Test
    public void testGetAllTableNames(){
        db.setConnection("ImportProcessing","postgres","admin");
//        Object[][] tables = db.Tables();
//        String[] strTables = new String[tables.length];
//        for ( int i = 0; i < tables.length; i++ ) {
//            strTables[i] = tables[1][0].toString();
//        }
//        assertEquals("[__contractDPD, __contractDPD, __contractDPD,",
//                Arrays.toString(strTables).substring(0, 45));

    }
}
