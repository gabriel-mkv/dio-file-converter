package com.gabrielmkv.file_converter.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.gabrielmkv.file_converter.model.Transaction;
import com.gabrielmkv.file_converter.service.template.ReportGeneratorTemplate;

import tools.jackson.databind.ObjectMapper;

@Component("json")
public class ReportJSON extends ReportGeneratorTemplate {
    
    private final ObjectMapper objectMapper;

    public ReportJSON(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected byte[] generateContent(List<Transaction> transactions) {
        return objectMapper.writeValueAsBytes(transactions);
    }
    
}
