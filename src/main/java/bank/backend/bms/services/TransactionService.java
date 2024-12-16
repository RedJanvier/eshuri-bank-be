package bank.backend.bms.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bank.backend.bms.models.Transaction;
import bank.backend.bms.models.TransactionEnums.TransactionStatus;
import bank.backend.bms.repositories.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
    }
    
    public Transaction createTransaction(Transaction transaction){
        if(transaction.getTransactionStatus() == null){
            transaction.setTransactionStatus(TransactionStatus.PENDING);
        }
        if(transaction.getTransactionDate()== null){
            transaction.setTransactionDate(LocalDateTime.now());
        }
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (optionalTransaction.isEmpty()) {
            throw new RuntimeException("Transaction not found with ID: " + id);
        }

        Transaction existingTransaction = optionalTransaction.get();
        existingTransaction.setTransactionStatus(updatedTransaction.getTransactionStatus());
        return transactionRepository.save(existingTransaction);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with ID: " + id);
        }
        transactionRepository.deleteById(id);
    }

}