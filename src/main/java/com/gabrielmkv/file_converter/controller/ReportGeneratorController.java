package com.gabrielmkv.file_converter.controller;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielmkv.file_converter.exception.ReportGenerationException;
import com.gabrielmkv.file_converter.service.template.ReportGeneratorTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Map;

/**
 * Controlador REST responsável pelos endpoints de geração de relatórios.
 * Gerencia as requisições para exportação de dados de transações.
 */
@RestController
@RequestMapping("/report")
public class ReportGeneratorController {

    @Autowired
    private Map<String, ReportGeneratorTemplate> service;

    /**
     * Gera um relatório de transações no formato solicitado.
     * 
     * @param type O formato do arquivo desejado (pdf, csv, json).
     * @return ResponseEntity contendo os bytes do arquivo gerado.
     */
    @Operation(summary = "Gera relatório de transações", description = "Busca todas as transações do banco e gera um arquivo no formato especificado.")
    @GetMapping
    public ResponseEntity<byte[]> reportGenerate(
            @Parameter(description = "Formato do arquivo: pdf | csv (faz download) ou json (abre no browser)") @RequestParam(name = "format") String type) {
        ReportGeneratorTemplate strategy = service.get(type);

        if (strategy == null) {
            throw new ReportGenerationException("Formato de relatório não suportado: " + type);
        }

        byte[] report = strategy.generateReport();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(strategy.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        strategy.getContentDisposition() + "; filename=relatorio." + type)
                .body(report);
    }

}
