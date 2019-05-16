package netcracker;

import com.fasterxml.jackson.annotation.*;

@JsonAutoDetect
public class BankAccount {

    private long number;
    private int sum=0;
    @JsonCreator
    public BankAccount(@JsonProperty("number")long number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

}
