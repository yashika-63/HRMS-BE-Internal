package com.spectrum.PDFProcessing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
public class PdfResumeController {
    private final PdfResumeService pdfResumeService;

    public PdfResumeController(PdfResumeService pdfResumeService) {
        this.pdfResumeService = pdfResumeService;
    }
    @PostMapping("/shortlist")
    public ResponseEntity<List<Map<String, String>>> shortlistResumes(
            @RequestParam("files") MultipartFile[] files) throws IOException {
    
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    
        List<Map<String, String>> candidates = pdfResumeService.extractResumes(Arrays.asList(files));
        List<Map<String, String>> shortlisted = pdfResumeService.shortlistCandidates(candidates);
        return ResponseEntity.ok(shortlisted);
    }
}
