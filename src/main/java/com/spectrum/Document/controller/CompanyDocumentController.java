package com.spectrum.Document.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.spectrum.Document.model.CompanyDocument;
import com.spectrum.Document.model.Document;
import com.spectrum.Document.repository.CompanyDocumentRepository;
import com.spectrum.Document.service.CompanyDocumentService;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

@RestController
@RequestMapping("/api/company-document")
public class CompanyDocumentController {

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private CompanyDocumentRepository companyDocumentRepository;

    @Autowired
    private CompanyDocumentService companyDocumentService;

    private static final Logger logger = LoggerFactory.getLogger(CompanyDocumentController.class);

    @PostMapping("/{companyId}/uploadCompanyDocument")
public ResponseEntity<String> uploadCompanyDocument(
        @PathVariable Long companyId,
        @RequestParam("file") MultipartFile file,
        @RequestParam("documentIdentityKey") String documentIdentityKey) {

    logger.info("Uploading file for company ID: {}", companyId);

    try {
        String fileName = file.getOriginalFilename();
        logger.info("File name received: {}", fileName);
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid file name");
        }
        // Path path = Paths.get("C:/Users/spectrum/Desktop/Employee/" + fileName);
        Path path = Paths.get("/home/ubuntu/DocumentHRMS/expensedocument/" + fileName);

        // Path path = Paths.get("C:/Users/pristine/Desktop/HRMSDocuments/Company/" + fileName);

        
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // 1. Check if a document with the same key and status = true exists
        CompanyDocument existingActiveDoc = companyDocumentRepository
                .findByDocumentIdentityKeyAndStatus(documentIdentityKey, true);

        if (existingActiveDoc != null) {
            existingActiveDoc.setStatus(false);
            companyDocumentRepository.save(existingActiveDoc); // Update old one to inactive
        }

        // 2. Prepare new document
        CompanyDocument newDocument = new CompanyDocument(path.toString());

        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        newDocument.setCompany(company);
        newDocument.setStatus(true);
        newDocument.setDocumentIdentityKey(documentIdentityKey);

        companyDocumentRepository.save(newDocument);

        return ResponseEntity.ok("Company document uploaded and previous one deactivated (if existed). Path: " + path);
    } catch (IOException e) {
        logger.error("IOException during company document upload: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
    } catch (RuntimeException e) {
        logger.error("RuntimeException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
        logger.error("Unexpected error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
    }
}





@GetMapping("/view/{id}")
public ResponseEntity<?> viewFile(@PathVariable Long id) {
    try {
        // Fetch the document by its ID
        CompanyDocument companyDocument = companyDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));

        // Get the file path from the document
        Path filePath = Paths.get(companyDocument.getFilePath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found at path: " + companyDocument.getFilePath());
        }

        // Load the file as a resource
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File is not readable or does not exist.");
        }

        // Set the content type dynamically based on the file
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(resource);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while retrieving the file: " + e.getMessage());
    }
}




@GetMapping("/view/active")
public ResponseEntity<?> viewActiveDocumentByCompanyId(@RequestParam Long companyId) {
    try {
        List<CompanyDocument> activeDocs = companyDocumentRepository.findAllByCompanyIdAndStatus(companyId, true);

        if (activeDocs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No active document found for company ID: " + companyId);
        }

        // Optionally return the latest one (e.g., last added)
        CompanyDocument latestDoc = activeDocs.get(activeDocs.size() - 1);
        Path filePath = Paths.get(latestDoc.getFilePath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found at path: " + latestDoc.getFilePath());
        }

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File is not readable or does not exist.");
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(resource);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while retrieving the active document: " + e.getMessage());
    }
}



@GetMapping("/view/activenew")
public ResponseEntity<?> viewActiveDocumentByCompanyIdAndKey(
        @RequestParam Long companyId,
        @RequestParam String documentIdentityKey) {
    try {
        List<CompanyDocument> activeDocs = companyDocumentRepository
                .findAllByCompanyIdAndDocumentIdentityKeyAndStatus(companyId, documentIdentityKey, true);

        if (activeDocs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No active document found for the given company and key.");
        }

        // You can choose latest or the first
        CompanyDocument latestDoc = activeDocs.get(activeDocs.size() - 1);
        Path filePath = Paths.get(latestDoc.getFilePath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found at path: " + latestDoc.getFilePath());
        }

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File is not readable or does not exist.");
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(resource);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while retrieving the active document: " + e.getMessage());
    }
}



@GetMapping("/active/{companyId}")
public List<CompanyDocument> getActiveDocuments(@PathVariable Long companyId) {
    return companyDocumentService.getActiveDocumentsByCompanyId(companyId);
}


}
