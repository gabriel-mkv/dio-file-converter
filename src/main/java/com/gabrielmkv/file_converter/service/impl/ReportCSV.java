package com.gabrielmkv.file_converter.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.gabrielmkv.file_converter.service.template.ReportGeneratorTemplate;
import com.gabrielmkv.file_converter.exception.ReportGenerationException;
import com.gabrielmkv.file_converter.model.Transaction;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.math.BigDecimal;

@Component("csv")
public class ReportCSV extends ReportGeneratorTemplate {

    private final CsvMapper mapper;
    private final CsvSchema schema;

    public ReportCSV() {
        SimpleModule decimalModule = new SimpleModule();
        decimalModule.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toPlainString().replace(".", ","));
            }
        });

        this.mapper = CsvMapper.builder()
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new JavaTimeModule())
                .addModule(decimalModule)
                .defaultLocale(Locale.of("pt", "BR"))
                .build();

        this.schema = mapper.schemaFor(Transaction.class)
                    .withHeader()
                    .withColumnSeparator(';')
                    .withLineSeparator("\n");
    }

    @Override
    public String getMimeType() {
        return "text/csv;charset=UTF-8";
    }

    @Override
    protected byte[] generateContent(List<Transaction> transactions) {
        try {
            return mapper.writer(schema).writeValueAsBytes(transactions);
        } catch (JsonProcessingException e) {
            throw new ReportGenerationException("Falha ao processar os dados para o CSV: " + e.getMessage(), e);
        }
    }
}
