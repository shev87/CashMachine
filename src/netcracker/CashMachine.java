package netcracker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import netcracker.exceptions.InterruptOperationException;

import java.io.*;
import java.util.*;

public class CashMachine {
    private static CashMachine cashMachine;
    private static BankAccount bankAccount = null;
    private final Map<Integer, Integer> moneyCashMachine = new HashMap<>();
    public static final String RESOURCE_PATH = "netcracker/resourses/";
    private File jsonFile = new File("./src/netcracker/resourses/bankAccounts");

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        ResourceBundle res = ResourceBundle.getBundle(RESOURCE_PATH + "common", Locale.ENGLISH);
        try {
            CommandExecutor.execute(Operation.LOGIN);
            Operation operation;
            do {
                ConsoleHelper.writeMessage(res.getString("choose.operation") + " \n" +
                        res.getString("operation.INFO") + ": 1;\n" +
                        res.getString("operation.DEPOSIT") + ": 2;\n" +
                        res.getString("operation.WITHDRAW") + ": 3;\n" +
                        res.getString("operation.EXIT") + ": 4");
                operation = ConsoleHelper.askOperation();

                CommandExecutor.execute(operation);
            }
            while (operation != Operation.EXIT);
        } catch (InterruptOperationException e) {
            try {
                CommandExecutor.execute(Operation.EXIT);
            } catch (InterruptOperationException ignored) {
            }
            ConsoleHelper.printExitMessage();
        }
    }

    private CashMachine(){}

    public static CashMachine getCashMachine() {
        if (cashMachine == null) cashMachine = new CashMachine();
        return cashMachine;
    }

    public Map<Integer, Integer> getMoneyCashMachine(){
        try {
            File file = new File("./src/netcracker/resourses/amount");
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            for (String key : properties.stringPropertyNames())
            {
                moneyCashMachine.put(Integer.parseInt(key), Integer.parseInt(properties.getProperty(key)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moneyCashMachine;
    }

    public void setMoneyCashMachine(Map<Integer, Integer> map){
        try {
            File file = new File("./src/netcracker/resourses/amount");
            Properties properties = new Properties();
            for (Map.Entry<Integer, Integer> pair : map.entrySet()){
              properties.setProperty(pair.getKey().toString(), pair.getValue().toString());
            }
            properties.store(new FileWriter(file), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTotalAmountCashMachine() {
        int result = 0;
        for (Map.Entry<Integer, Integer> pair : getMoneyCashMachine().entrySet()) {
            result = result + (pair.getKey() * pair.getValue());
        }
        return result;
    }

    public List<BankAccount> loadBankAccounts(){
        try {
            FileReader reader = new FileReader(jsonFile);
            ObjectMapper mapper = new ObjectMapper();
            List<BankAccount> list = mapper.readValue(reader, new TypeReference<List<BankAccount>>(){});
            return list;
        } catch (MismatchedInputException e) {
            return new ArrayList<>();
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public void writeListBankAccounts(List<BankAccount> list){
        try {
            FileWriter writer = new FileWriter(jsonFile);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBankAccount(BankAccount bankAccount){
        List<BankAccount> list = loadBankAccounts();
        for (BankAccount b : list){
            if (b.getNumber() == bankAccount.getNumber()){
                b.setSum(bankAccount.getSum());
            }
        }
        writeListBankAccounts(list);
    }

    public static BankAccount getBankAccount() {
        return bankAccount;
    }

    public static void setBankAccount(BankAccount bankAccount) {
        CashMachine.bankAccount = bankAccount;
    }
}
