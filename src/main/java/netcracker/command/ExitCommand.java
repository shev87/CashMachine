package netcracker.command;

import netcracker.CashMachine;
import netcracker.ConsoleHelper;
import netcracker.exceptions.InterruptOperationException;

import java.util.ResourceBundle;

public class ExitCommand implements Command{
    private final ResourceBundle res = ResourceBundle
            .getBundle(CashMachine.RESOURCE_PATH + "exit");

    @Override
    public void execute() throws InterruptOperationException {
            ConsoleHelper.writeMessage(res.getString("thank.message"));
    }
}
