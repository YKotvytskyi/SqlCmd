package ua.com.juja.SqlCmd;

import ua.com.juja.SqlCmd.DdTypes.DBTypeConstPosgree;

import java.util.Arrays;

public class JdbcMsSql {

    public static void main(String[] args) {

        //DatabaseManager db = DatabaseManager.getInstance(new DBTypeConstMssql());
        DatabaseManager db = DatabaseManager.getInstance(new DBTypeConstPosgree());
        db.setConnection("testdb","postgres","admin");

        System.out.println(db.getConnection().toString());

        System.out.println( db.Tables().toString());

        System.out.println( db.Create(new String[]{"myTable1","r1","r2"})
                .toString()
                + "\n Create\n---\n\n");

        System.out.println(db.Insert(new String[]{"myTable1","r1","0","r2","11 22"}).toString()
                + "\nInsert\n---\n\n");

//        System.out.println(db.Find1("myTable1").toString()
//                + "\nFind1\n---\n\n");

        System.out.println(db.Drop("myTable1").toString()
                +"\nDrop\n---\n\n");

//
//        showResult(db.Insert(new String[]{"myTable1","r1","1","r2","4567"}),
//                "Insert");
//
//        showResult(db.Find1("myTable1"),
//                "Find1");



//        showResult(db.Tables() ,
//                "Tables" ) ;
//
//        showResult(db.Create(new String[]{"myTable1","r1","r2"}) ,
//                "Create" ) ;
//
//        showResult(db.Insert(new String[]{"myTable1","r1","0","r2","11 22"}),
//                "Insert");
//
//        showResult(db.Insert(new String[]{"myTable1","r1","1","r2","4567"}),
//                "Insert");
//
//        showResult(db.Find1("myTable1"),
//                "Find1");
//
//        showResult(db.Update(new String[]{"myTable1","r1","0","r2","1234"}),
//                "Update");
//
//        showResult(db.Find1("myTable1"),
//                "Find1");
//
//        showResult(db.Delete(new String[]{"myTable1","r2","1234"}),
//                "Delete");
//
//        showResult(db.Find1("myTable1"),
//                "Find1");
//
//        showResult(db.Clear("myTable1"),
//                "Clear");
//
//        showResult(db.Find1("myTable1"),
//                "Find1");
//
//        showResult(db.Drop("myTable1"),
//                "Drop");

        System.out.println("Ok!");
    }

    static void showResult(Object[][] rowSet, String command){
        System.out.println(command);
        if (rowSet != null) {
            for (Object[] row: rowSet) {
                System.out.println("\t" + Arrays.toString(row));
            }
        }
    }
}