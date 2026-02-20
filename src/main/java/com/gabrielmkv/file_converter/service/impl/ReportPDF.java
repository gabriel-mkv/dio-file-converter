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

@Component("pdf")
public class ReportPDF extends ReportGeneratorTemplate {

    private static final Font BODY_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);

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

    private PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(240, 240, 240));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

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
