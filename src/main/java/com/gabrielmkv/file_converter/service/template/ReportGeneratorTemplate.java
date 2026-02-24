package com.gabrielmkv.file_converter.service.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gabrielmkv.file_converter.model.Transaction;
import com.gabrielmkv.file_converter.repository.TransactionRepository;
import com.gabrielmkv.file_converter.exception.ReportGenerationException;

/**
 * Classe abstrata que define o template (Template Method) para a geração de relatórios.
 * Estrutura o algoritmo de geração, permitindo que subclasses (Strategies)
 * implementem os detalhes específicos de cada formato de arquivo.
 */
public abstract class ReportGeneratorTemplate {
    
    @Autowired
    private TransactionRepository repository;

    private final List<Transaction> extractData() {
        return repository.findAll();
    }

    /**
     * Retorna o MIME type do arquivo gerado.
     * 
     * @return String com o MIME type.
     */
    public abstract String getMimeType();

    /**
     * Define o comportamento do navegador ao receber o arquivo.
     * 'inline' tenta exibir no navegador, 'attachment' força o download.
     * 
     * @return String com o tipo de content disposition.
     */
    public String getContentDisposition() {
        return "inline";
    }

    /**
     * Método abstrato a ser implementado pelas subclasses para gerar o conteúdo
     * específico do relatório (PDF, CSV, JSON, etc.).
     * 
     * @param transactions A lista de transações a serem incluídas no relatório.
     * @return Um array de bytes representando o conteúdo do arquivo.
     */
    protected abstract byte[] generateContent(List<Transaction> transactions);

    /**
     * Orquestra a geração do relatório. Este é o Template Method.
     * 
     * @return Um array de bytes contendo o relatório completo.
     * @throws ReportGenerationException se não houver dados ou se ocorrer um erro na geração.
     */
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
