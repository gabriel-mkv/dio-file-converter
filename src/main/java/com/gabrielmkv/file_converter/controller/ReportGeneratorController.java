package com.gabrielmkv.file_converter.controller;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielmkv.file_converter.exception.ReportGenerationException;
import com.gabrielmkv.file_converter.service.template.ReportGeneratorTemplate;

import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportGeneratorController {

    @Autowired
    private Map<String, ReportGeneratorTemplate> service;

    @GetMapping("/{type}")
    public ResponseEntity<byte[]> reportGenerate(@PathVariable String type) {
        ReportGeneratorTemplate strategy = service.get(type);

        if (strategy == null) {
            throw new ReportGenerationException("Formato de relatório não suportado: " + type);
        }

        byte[] report = strategy.generateReport();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(strategy.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio." + type)
                .body(report);
    }

}
