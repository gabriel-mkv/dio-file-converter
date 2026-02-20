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
            byte[] content = generateContent(transactions);
            return content;
        } catch (Exception e) {
            System.err.println("Erro ao gerar o relatório: " + e.getMessage());
            throw new ReportGenerationException("Erro ao gerar o relatório!");
        }
    }

}
