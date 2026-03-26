package com.leo.ai.ollamachat.ingestion.service;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.Loader; // Importação necessária para a v3.x
import org.apache.pdfbox.io.RandomAccessReadBuffer; // Para lidar com InputStream
import java.io.File;
import java.io.InputStream;

@Service
public class PdfParserService {

    /**
     * Extrai o texto de um PDF a partir de um InputStream
     */
	public String extractText(InputStream inputStream) {
	    // Na v3.x, usamos Loader.loadPDF
	    // O MemoryUsageSetting agora é tratado de forma diferente ou omitido para o padrão
	    try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(inputStream))) {

	        PDFTextStripper stripper = new PDFTextStripper();
	        return stripper.getText(document);

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to parse PDF", e);
	    }
	}
}