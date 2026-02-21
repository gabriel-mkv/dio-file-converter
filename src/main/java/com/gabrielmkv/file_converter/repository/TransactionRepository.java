package com.gabrielmkv.file_converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielmkv.file_converter.model.Transaction;

/**
 * Repositório Spring Data JPA para a entidade {@link Transaction}.
 * Fornece métodos para operações de banco de dados (CRUD) com transações.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
}
