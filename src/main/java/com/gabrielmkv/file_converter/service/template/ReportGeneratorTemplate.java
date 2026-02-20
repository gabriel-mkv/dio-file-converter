package com.gabrielmkv.file_converter.service.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gabrielmkv.file_converter.model.Transaction;
import com.gabrielmkv.file_converter.repository.TransactionRepository;
import com.gabrielmkv.file_converter.exception.ReportGenerationException;

public abstract class ReportGeneratorTemplate {
    
    @Autowired
    private TransactionRepository repository;

    private final List<Transaction> extractData() {
        return repository.findAll();
    }

    protected abstract byte[] generateContent(List<Transaction> transactions);

    public byte[] generateReport() {
        try {
            List<Transaction> transactions = extractData();

            if (transactions == null || transactions.isEmpty()) {
                throw new ReportGenerationException("Nenhuma transação encontrada!");
            }

            return generateContent(transactions);
        } catch (Exception e) {
            throw new ReportGenerationException("Erro ao gerar o relatório: " + e.getMessage(), e);
        }
    }

}
