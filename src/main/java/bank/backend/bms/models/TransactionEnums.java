package bank.backend.bms.models;

public class TransactionEnums {
    public enum TransactionType{
        DEPOSIT,
        WITHDRAWL,
        TRANSFER
    }
    public enum TransactionStatus{
        PENDING,
        SUCCESS,
        FAILED
    }
}
