package ua.com.juja.SqlCmd.controller.Command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.SqlCmd.controller.command.Command;
import ua.com.juja.SqlCmd.controller.command.Exit;
import ua.com.juja.SqlCmd.controller.command.ExitException;

import static org.junit.Assert.*;

public class ExitTest {

    //given
    private FakeView view = new FakeView();
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
            assertEquals ("До скорой встречи!\n",
                view.getWritten());
    }

}
