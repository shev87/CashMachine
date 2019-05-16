package netcracker;

import netcracker.exceptions.InterruptOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleHelper {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final ResourceBundle res = ResourceBundle
            .getBundle(CashMachine.RESOURCE_PATH + "common");

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static void printExitMessage() {
        ConsoleHelper.writeMessage(res.getString("the.end"));
    }

    public static String readString() throws InterruptOperationException {
        String message = "";
        try {
            message = reader.readLine();
            if (message.equalsIgnoreCase(res.getString("operation.EXIT"))) {
                throw new InterruptOperationException();
            }
        } catch (IOException ignored) {
        }
        return message;
    }

    public static String[] getValidTwoDigits() throws InterruptOperationException {
        String[] array;
        writeMessage(res.getString("choose.denomination.and.count.format"));

        while (true) {
            String s = readString();
            array = s.split(" ");
            int k;
            int l;
            try {
                k = Integer.parseInt(array[0]);
                l = Integer.parseInt(array[1]);
            } catch (Exception e) {
                writeMessage(res.getString("invalid.data"));
                continue;
            }
            if (k <= 0 || l <= 0 || array.length > 2) {
                writeMessage(res.getString("invalid.data"));
                continue;
            }
            if (!(k == 100 || k == 200 || k == 500 || k == 1000 || k == 2000 ||k == 5000)){
                writeMessage(res.getString("invalid.data"));
                continue;
            }
            break;
        }
        return array;
    }

    public static Operation askOperation() throws InterruptOperationException {
        while (true) {
            String line = readString();
            if (checkWithRegExp(line)) {
                return Operation.getAllowableOperationByOrdinal(Integer.parseInt(line));
            } else {
                writeMessage(res.getString("invalid.data"));
            }
        }

    }

    private static boolean checkWithRegExp(String Name) {
        Pattern p = Pattern.compile("^[1-4]$");
        Matcher m = p.matcher(Name);
        return m.matches();
    }
}
