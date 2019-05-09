package netcracker.command;

import netcracker.BankAccount;
import netcracker.CashMachine;
import netcracker.ConsoleHelper;
import netcracker.CurrencyManipulator;
import netcracker.exceptions.InterruptOperationException;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class InfoCommand implements Command{
    private final ResourceBundle res = ResourceBundle
            .getBundle(CashMachine.RESOURCE_PATH + "info");
    //private Map<Integer, Integer> moneyCashMachine = new HashMap<>();
    @Override
    public void execute() {
        ConsoleHelper.writeMessage(res.getString("before"));
        BankAccount bankAccount = CashMachine.getBankAccount();
        int sum = bankAccount.getSum();
        /*moneyCashMachine = cashMachine.getMoneyCashMachine();
        for (Map.Entry<Integer, Integer> pair : moneyCashMachine.entrySet()){
            System.out.println(pair.getKey() + "=" + pair.getValue());
        }
        sum = cashMachine.getTotalAmountCashMachine();
        */
        if (sum > 0) {
            ConsoleHelper.writeMessage(sum + " RUB");
        } else if (sum == 0) {
            ConsoleHelper.writeMessage(res.getString("no.money"));
        }


    }
}
