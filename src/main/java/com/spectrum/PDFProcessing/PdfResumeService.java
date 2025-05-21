package com.spectrum.PDFProcessing;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfResumeService {

    public List<Map<String, String>> extractResumes(List<MultipartFile> files) throws IOException {
        List<Map<String, String>> candidates = new ArrayList<>();
    
        for (MultipartFile file : files) {
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();
    
            System.out.println("Extracted Text from PDF: \n" + text);  // Debug Log
    
            Map<String, String> candidate = extractCandidateDetails(text);
            candidates.add(candidate);
        }
        return candidates;
    }
    

    private Map<String, String> extractCandidateDetails(String text) {
        Map<String, String> candidate = new HashMap<>();

        // Extract Name (First Line as Name Example)
        String[] lines = text.split("\n");
        if (lines.length > 0) candidate.put("name", lines[0].trim());

        // Extract Email
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}");
        Matcher matcher = emailPattern.matcher(text);
        String email = matcher.find() ? matcher.group() : "Not Found";
        candidate.put("email", email);
        
        // Extract Skills
        List<String> skillKeywords = List.of("Java", "Spring Boot", "React", "Node.js", "Python");
        List<String> skills = new ArrayList<>();
        for (String skill : skillKeywords) {
            if (text.toLowerCase().contains(skill.toLowerCase())) {
                skills.add(skill);
            }
        }
        candidate.put("skills", String.join(", ", skills));

        // Extract Experience (Simple Pattern for "X years experience")
        String experience = text.matches(".*(\\d+\\s+years).*") ?
                text.replaceAll(".*?(\\d+\\s+years).*", "$1") : "Not Found";
        candidate.put("experience", experience);

        return candidate;
    }

    public List<Map<String, String>> shortlistCandidates(List<Map<String, String>> candidates) {
        List<Map<String, String>> shortlisted = new ArrayList<>();

        for (Map<String, String> candidate : candidates) {
            if (candidate.get("skills").contains("HTML") && candidate.get("skills").contains("CSS")) {
                shortlisted.add(candidate);
            }
        }
        return shortlisted;
    }
}
