package bank.backend.bms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.backend.bms.models.Transaction;

@Repository
public interface  TransactionRepository extends JpaRepository<Transaction, Long>{}
