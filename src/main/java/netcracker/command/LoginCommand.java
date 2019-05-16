package netcracker.command;

import netcracker.BankAccount;
import netcracker.CashMachine;
import netcracker.ConsoleHelper;
import netcracker.exceptions.InterruptOperationException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginCommand implements Command{
    private final ResourceBundle validCreditCards = ResourceBundle
            .getBundle(CashMachine.RESOURCE_PATH + "verifiedCards");
    private final ResourceBundle res = ResourceBundle
            .getBundle(CashMachine.RESOURCE_PATH + "login");


    List<BankAccount> list = new ArrayList<>();

    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("before"));
        while (true) {
            ConsoleHelper.writeMessage(res.getString("specify.data"));
            String s1 = ConsoleHelper.readString();
            String s2 = ConsoleHelper.readString();
            if (validCreditCards.containsKey(s1)) {
                String s = "";
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    byte[] bytes = messageDigest.digest(s2.getBytes());
                    for (byte b : bytes){
                        s += String.format("%02x", b);
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if (validCreditCards.getString(s1).equals(s)) {
                    ConsoleHelper.writeMessage(String.format(res.getString("success.format"), s1));
                    CashMachine cashMachine = CashMachine.getCashMachine();
                    list = cashMachine.loadBankAccounts();
                    for (BankAccount b : list){
                        if (b.getNumber() == Long.parseLong(s1)) {
                            CashMachine.setBankAccount(b);
                            cashMachine.writeListBankAccounts(list);
                            break;
                        }
                    }

                    if (CashMachine.getBankAccount() == null) {
                        ConsoleHelper.writeMessage(res.getString("visit.first"));
                        CashMachine.setBankAccount(new BankAccount(Long.parseLong(s1)));
                        ConsoleHelper.writeMessage(res.getString("account.created"));
                        list.add(CashMachine.getBankAccount());
                        cashMachine.writeListBankAccounts(list);
                    }

                } else {
                    ConsoleHelper.writeMessage(String.format(res.getString("not.verified.format"), s1));
                    ConsoleHelper.writeMessage(res.getString("try.again.or.exit"));
                    continue;
                }
            } else {
                ConsoleHelper.writeMessage(String.format(res.getString("not.verified.format"), s1));
                ConsoleHelper.writeMessage(res.getString("try.again.with.details"));
                continue;
            }
            break;
        }
    }


}
