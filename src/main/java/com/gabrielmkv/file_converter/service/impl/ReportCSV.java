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

/**
 * Implementação da estratégia de geração de relatórios em formato CSV.
 * <p>
 * Utiliza a biblioteca Jackson (com o módulo CSV) para serializar objetos.
 * O arquivo gerado utiliza ponto e vírgula (;) como separador e segue o padrão
 * de formatação brasileiro para números e datas.
 * </p>
 */
@Component("csv")
public class ReportCSV extends ReportGeneratorTemplate {

    // Mapper e Schema são configurados no construtor para serem reutilizados e thread-safe.
    private final CsvMapper mapper;
    private final CsvSchema schema;

    /**
     * Construtor da classe.
     * <p>
     * Configura o {@link CsvMapper} e o {@link CsvSchema} com as seguintes definições:
     * <ul>
     *   <li>Módulo JavaTimeModule para datas.</li>
     *   <li>Serializer customizado para BigDecimal (formato brasileiro).</li>
     *   <li>Schema com cabeçalho e separador ';'.</li>
     * </ul>
     * </p>
     */
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

    /**
     * Gera o conteúdo do arquivo CSV.
     * 
     * @param transactions A lista de transações a serem serializadas.
     * @return Um array de bytes representando o arquivo CSV.
     * @throws ReportGenerationException Se ocorrer um erro na serialização (JsonProcessingException).
     */
    @Override
    protected byte[] generateContent(List<Transaction> transactions) {
        try {
            return mapper.writer(schema).writeValueAsBytes(transactions);
        } catch (JsonProcessingException e) {
            throw new ReportGenerationException("Falha ao processar os dados para o CSV: " + e.getMessage(), e);
        }
    }
}
