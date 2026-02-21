package com.gabrielmkv.file_converter.service.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openpdf.text.Chunk;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Component;

import com.gabrielmkv.file_converter.exception.ReportGenerationException;
import com.gabrielmkv.file_converter.model.Transaction;
import com.gabrielmkv.file_converter.service.template.ReportGeneratorTemplate;

/**
 * Implementação da estratégia de geração de relatórios em formato PDF.
 * <p>
 * Esta classe utiliza a biblioteca OpenPDF para criar documentos PDF formatados.
 * O relatório gerado contém um cabeçalho com título e data, seguido por uma tabela
 * listando todas as transações com formatação zebrada para melhor leitura.
 * </p>
 */
@Component("pdf")
public class ReportPDF extends ReportGeneratorTemplate {

    private static final Font BODY_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);

    @Override
    public String getMimeType() {
        return "application/pdf";
    }

    @Override
    public String getContentDisposition() {
        return "attachment";
    }

    /**
     * Gera o conteúdo do arquivo PDF.
     * <p>
     * Cria um documento A4, abre o stream de escrita, adiciona os metadados,
     * o cabeçalho e a tabela de dados.
     * </p>
     * 
     * @param transactions A lista de transações a serem incluídas no relatório.
     * @return Um array de bytes representando o arquivo PDF.
     * @throws ReportGenerationException Se ocorrer um erro na biblioteca de PDF (DocumentException).
     */
    @Override
    protected byte[] generateContent(List<Transaction> transactions) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, out);

            document.open();

            setHeaderDocument(document, "Relatório de Transações");
            setTableInDocument(document, transactions);

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new ReportGenerationException("Erro ao gerar o relatório PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Configura e adiciona o cabeçalho ao documento.
     * 
     * @param document O documento PDF sendo gerado.
     * @param stringTitle O título do relatório.
     */
    private void setHeaderDocument(Document document, String stringTitle) {
        Paragraph title = new Paragraph(stringTitle, new Font(Font.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_LEFT);
        document.add(title);

        Paragraph date = new Paragraph(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                new Font(Font.HELVETICA, 10, Font.NORMAL));
        date.setSpacingAfter(10);
        document.add(date);

        document.add(new LineSeparator());
        document.add(Chunk.NEWLINE);
    }

    /**
     * Constrói e adiciona a tabela de transações ao documento.
     * <p>
     * A tabela possui 4 colunas (Data, Descrição, Valor, Categoria) e largura de 100%.
     * </p>
     * @param document O documento PDF.
     * @param transactions A lista de dados para preencher a tabela.
     */
    private void setTableInDocument(Document document, List<Transaction> transactions) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15f);
        table.setWidths(new float[] { 1.5f, 3f, 1.5f, 2f });

        table.addCell(createHeaderCell("Data"));
        table.addCell(createHeaderCell("Descrição"));
        table.addCell(createHeaderCell("Valor"));
        table.addCell(createHeaderCell("Categoria"));

        int rowIndex = 0;

        for (Transaction transaction : transactions) {
            table.addCell(createCell(transaction.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), rowIndex));
            table.addCell(createCell(transaction.getDescription(), rowIndex));
            table.addCell(createCell(transaction.getValue().toString().replace(".", ","), rowIndex));
            table.addCell(createCell(transaction.getCategory(), rowIndex));
            rowIndex++;
        }

        document.add(table);
    }

    /**
     * Cria uma célula de cabeçalho estilizada.
     * 
     * @param text O texto do cabeçalho.
     * @return Uma célula (PdfPCell) com fundo cinza e texto em negrito.
     */
    private PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(240, 240, 240));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    /**
     * Cria uma célula de dados estilizada com cores alternadas (zebrado).
     * 
     * @param text O conteúdo da célula.
     * @param rowIndex O índice da linha atual (usado para alternar a cor de fundo).
     * @return Uma célula (PdfPCell) formatada.
     */
    private PdfPCell createCell(String text, int rowIndex) {
        PdfPCell cell = new PdfPCell(new Phrase(text, BODY_FONT));
        cell.setPadding(6f);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderWidth(0.5f);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        if (rowIndex % 2 == 0) {
            cell.setBackgroundColor(new Color(252, 252, 252));
        } else {
            cell.setBackgroundColor(new Color(245, 245, 245));
        }

        return cell;
    }
}
