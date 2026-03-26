package com.leo.ai.ollamachat.rag.ingestion;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DocumentLoader {

    public String load(Path file) {

        String name = file.toString().toLowerCase();

        try {

            if (name.endsWith(".pdf")) {
                return loadPdf(file);
            }

            return Files.readString(file);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar documento: " + file, e);
        }
    }

    private String loadPdf(Path file) throws IOException {

        try (PDDocument document = Loader.loadPDF(file.toFile())) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);
        }
    }
}