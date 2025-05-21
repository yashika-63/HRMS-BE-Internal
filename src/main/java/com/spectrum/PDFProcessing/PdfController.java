package com.spectrum.PDFProcessing;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {
    private final PdfProcessingService pdfService;

    public PdfController(PdfProcessingService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/shortlist")
    public ResponseEntity<List<String>> processPdf(@RequestParam("file") MultipartFile file) throws IOException {
        List<String> records = pdfService.extractTextFromPdf(file);
        List<String> shortlisted = pdfService.shortlistRecords(records);
        return ResponseEntity.ok(shortlisted);
    }
}
