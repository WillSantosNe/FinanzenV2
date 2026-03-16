package com.finanzen.api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finanzen.api.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    
} 
