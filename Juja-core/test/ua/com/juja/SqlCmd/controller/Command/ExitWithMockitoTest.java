package ua.com.juja.SqlCmd.controller.Command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.SqlCmd.controller.command.Command;
import ua.com.juja.SqlCmd.controller.command.Exit;
import ua.com.juja.SqlCmd.controller.command.ExitException;
import ua.com.juja.SqlCmd.view.View;

import static org.junit.Assert.*;

public class ExitWithMockitoTest {

    //given
    private View view = Mockito.mock(View.class);
    private Command command = new Exit(view);

    @Test
    public void testCanProcessExitString(){

        //when
        boolean canProcess = command.canProcess("Exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString(){

        //when
        boolean canProcess = command.canProcess("Qwe");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommand_throwsExitException(){
        //when
        try{
            command.process("Exit");
            fail("Expected Exit Exception");
        }
        catch (ExitException e){
            // do nothing
        }

        //then
        Mockito.verify(view).write("До скорой встречи!");

    }

}