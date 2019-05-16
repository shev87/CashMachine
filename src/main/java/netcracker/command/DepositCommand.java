package netcracker.command;

import netcracker.BankAccount;
import netcracker.CashMachine;
import netcracker.ConsoleHelper;
import netcracker.exceptions.InterruptOperationException;

import java.util.*;

public class DepositCommand implements Command{
    private final ResourceBundle res = ResourceBundle
            .getBundle(CashMachine.RESOURCE_PATH + "deposit_en");
    private Map<Integer, Integer> moneyCashMachine = new HashMap<>();

    @Override
    public void execute() throws InterruptOperationException {
        BankAccount bankAccount = CashMachine.getBankAccount();
        ConsoleHelper.writeMessage(res.getString("before"));
        String s = "y";
        while (s.equalsIgnoreCase("y")) {
            String[] moneyAndAmount = ConsoleHelper.getValidTwoDigits();
            try {
                int k = Integer.parseInt(moneyAndAmount[0]);
                int l = Integer.parseInt(moneyAndAmount[1]);
                bankAccount.setSum(bankAccount.getSum() + (k * l));
                moneyCashMachine = CashMachine.getCashMachine().getMoneyCashMachine();
                int count = moneyCashMachine.get(k) + l;
                moneyCashMachine.put(k, count);
                CashMachine.getCashMachine().setMoneyCashMachine(moneyCashMachine);
                ConsoleHelper
                        .writeMessage(String.format(res.getString("success.format"), k * l));
                CashMachine.getCashMachine().writeBankAccount(bankAccount);
            } catch (NumberFormatException e) {
                ConsoleHelper.writeMessage(res.getString("invalid.data"));
            }
            ConsoleHelper.writeMessage(res.getString("ask.money.yet"));
            s = ConsoleHelper.readString();
        }
    }
}
