package netcracker.command;

import netcracker.BankAccount;
import netcracker.CashMachine;
import netcracker.ConsoleHelper;
import netcracker.exceptions.InterruptOperationException;
import netcracker.exceptions.NotEnoughMoneyException;
import netcracker.exceptions.TooManyBanknotes;

import java.util.*;

public class WithdrawCommand implements Command {
    private final ResourceBundle res = ResourceBundle
            .getBundle(CashMachine.RESOURCE_PATH + "withdraw_en");
    private int countsBanknotes = 40;

    @Override
    public void execute() throws InterruptOperationException {
        int sum;
        while (true) {
            ConsoleHelper.writeMessage(res.getString("before"));
            String s = ConsoleHelper.readString();
            try {
                sum = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                ConsoleHelper.writeMessage(res.getString("specify.amount"));
                continue;
            }
            if (sum <= 0) {
                ConsoleHelper.writeMessage(res.getString("specify.not.empty.amount"));
                continue;
            }
            if (!isAmountAvailable(sum)) {
                ConsoleHelper.writeMessage(res.getString("not.enough.money"));
                continue;
            }
            try {
                withdrawAmount(sum);
            } catch (NotEnoughMoneyException e) {
                ConsoleHelper.writeMessage(res.getString("exact.amount.not.available"));
                continue;
            } catch (TooManyBanknotes e){
                ConsoleHelper.writeMessage(String.format(res.getString("too.many.banknotes"), countsBanknotes));
                continue;
            }
            ConsoleHelper.writeMessage(String.format(res.getString("success.format"), sum));
            break;
        }

    }

    private void withdrawAmount(int expectedAmount) throws NotEnoughMoneyException, TooManyBanknotes{
        int sum = expectedAmount;
        int stopBanknotes = countsBanknotes;
        Map<Integer, Integer> moneyOfCashMachine = CashMachine.getCashMachine().getMoneyCashMachine();
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> map : moneyOfCashMachine.entrySet()){
            list.add(map.getKey());
        }
        Collections.sort(list);
        Collections.reverse(list);
        TreeMap<Integer, Integer> result = new TreeMap<>();
        for (Integer integer : list){
            int money = integer;
            int count = moneyOfCashMachine.get(money);
            while (true){
                if (sum < money || count <= 0 || stopBanknotes <= 0){
                    moneyOfCashMachine.put(money, count);
                    break;
                }
                sum -= money;
                count--;
                stopBanknotes--;

                if (result.containsKey(money)) {
                    result.put(money, result.get(money) + 1);
                } else {
                    result.put(money, 1);
                }
            }
        }
        if (sum > 0) {
            if (stopBanknotes <= 0) throw new TooManyBanknotes();
            throw new NotEnoughMoneyException();
        } else {
            for (Map.Entry<Integer, Integer> pair : result.entrySet()) {
                ConsoleHelper.writeMessage("\t" + pair.getKey() + " RUB - " + pair.getValue() + " banknote(s)");
            }
            CashMachine machine = CashMachine.getCashMachine();
            BankAccount account = CashMachine.getBankAccount();
            machine.setMoneyCashMachine(moneyOfCashMachine);
            account.setSum(account.getSum() - expectedAmount);
            machine.writeBankAccount(account);
            ConsoleHelper.writeMessage("Transaction was successful!");
        }

    }

    private boolean isAmountAvailable(int sum){
        return (sum < CashMachine.getCashMachine().getTotalAmountCashMachine());
    }
}
