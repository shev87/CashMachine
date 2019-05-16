package netcracker.command;

import netcracker.exceptions.InterruptOperationException;

public interface Command {
    void execute() throws InterruptOperationException;
}
