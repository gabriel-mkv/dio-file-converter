package com.gabrielmkv.file_converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielmkv.file_converter.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
}
