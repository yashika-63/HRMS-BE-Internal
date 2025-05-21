package com.spectrum.PDFProcessing;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class PdfProcessingService {

    public List<String> extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();

        // Convert text to records (e.g., split by newline)
        List<String> records = List.of(text.split("\n"));
        return records;
    }


    public List<String> shortlistRecords(List<String> records) {
    List<String> shortlisted = new ArrayList<>();

    for (String record : records) {
        if (record.contains("react") && record.contains("React")) {
            shortlisted.add(record);
        }
    }
    return shortlisted;
}

}