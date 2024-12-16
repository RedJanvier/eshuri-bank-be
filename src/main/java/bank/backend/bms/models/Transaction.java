package bank.backend.bms.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transaction type is required")
    private TransactionEnums.TransactionType transactionType;

    @Null
    // @NotNull(message = "Source account is required")
    private Long accountIdFrom;

    @Null
    private Long accountIdTo;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private double amount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transaction Status is required")
    private TransactionEnums.TransactionStatus transactionStatus;

    @NotNull(message = "Transaction date is required")
    // @PastOrPresent(message="Transaction date must not be in the future")
    private LocalDateTime transactionDate;


}
