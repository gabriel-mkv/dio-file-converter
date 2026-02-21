package com.gabrielmkv.file_converter.service.impl;

import java.util.List;

import com.gabrielmkv.file_converter.exception.ReportGenerationException;
import org.springframework.stereotype.Component;

import com.gabrielmkv.file_converter.model.Transaction;
import com.gabrielmkv.file_converter.service.template.ReportGeneratorTemplate;

import tools.jackson.databind.ObjectMapper;

/**
 * Implementação da estratégia de geração de relatórios em formato JSON.
 * Utiliza o ObjectMapper do Jackson para serializar a lista de transações.
 * Esta classe é registrada no contexto Spring com o nome "json".
 */
@Component("json")
public class ReportJSON extends ReportGeneratorTemplate {

    private final ObjectMapper objectMapper;

    /**
     * Construtor que injeta o ObjectMapper configurado pelo Spring Boot.
     * @param objectMapper O mapper para converter objetos Java em JSON.
     */
    public ReportJSON(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    protected byte[] generateContent(List<Transaction> transactions) {
        try {
            return objectMapper.writeValueAsBytes(transactions);
        } catch (Exception e) {
            throw new ReportGenerationException("Falha ao processar os dados para o JSON: " + e.getMessage(), e);
        }
    }

}
