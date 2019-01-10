package ua.com.juja.SqlCmd.controller.Command;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.SqlCmd.controller.command.Command;
import ua.com.juja.SqlCmd.controller.command.Find;
import ua.com.juja.SqlCmd.model.DatabaseManager;
import ua.com.juja.SqlCmd.model.Table;
import ua.com.juja.SqlCmd.view.View;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertTrue;

public class FindTest {
    //given

    private DatabaseManager dm;
    private Command command;
    private View view;


    @Before
    public void setup(){
        dm = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Find(dm, view);
    }


    @Test
    public void testCanProcessFindString(){

        //when
        boolean canProcess = command.canProcess("Find|");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString(){
        //when
        boolean canProcess = command.canProcess("Qwe");

        //then
        Assert.assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommand(){
        when(dm.TableExist("my_table1"))
                .thenReturn(new Table(new Object[][] {{"my_table1"}} , new String[] {"Result"}));

        when(dm.Find("my_table1"))
                .thenReturn(new Table(new Object[][] {{"val0","val1"}} ,
                        new String[] {"Col0","Col1"}));

        command.process("Find|my_table1");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[+------+------+, + Col0 + Col1 +, +------+------+,"
                +" + val0 + val1 +, +------+------+]",
                captor.getAllValues().toString());

    }

}
