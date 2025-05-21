package com.spectrum.PreOnboarding.EmployeeVerification.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spectrum.Document.controller.CompanyDocumentController;
import com.spectrum.Document.model.CompanyDocument;
import com.spectrum.Document.model.DocumentExpenseManagement;
import com.spectrum.PreOnboarding.EmployeeVerification.model.EmployeeVerificationDocument;
import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicket;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.EmployeeVerificationDocumentRepository;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.VerificationTicketRepository;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.PreOnboarding.PreRegistration.repository.PreRegistrationRepository;
import com.spectrum.model.CompanyRegistration;

@RestController
@RequestMapping("/api/employeeVerificationDocument")

public class EmployeeVerificationDocumentController {


    @Autowired
    private EmployeeVerificationDocumentRepository employeeVerificationDocumentRepository;


    @Autowired
    private VerificationTicketRepository verificationTicketRepository;

    @Autowired
    private PreRegistrationRepository preRegistrationRepository;



    private static final Logger logger = LoggerFactory.getLogger(CompanyDocumentController.class);


    @PostMapping("/{verificationTicketId}/{preRegistrationId}/uploadEmployeeVerificationDocument")
public ResponseEntity<String> uploadEmployeeVerificationDocument(
        @PathVariable Long verificationTicketId,
        @PathVariable Long preRegistrationId,
        @RequestParam("file") MultipartFile file,
        @RequestParam("label") String label
) {
    logger.info("Uploading file for verification ticket ID: {}", verificationTicketId);
    logger.info("Uploading file for preregistration ID: {}", preRegistrationId);
    logger.info("Label received: {}", label);

    try {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid file name");
        }

        // Path path = Paths.get("C:/Users/spectrum/Desktop/Employee/" + fileName);
        Path path = Paths.get("/home/ubuntu/DocumentHRMS/alldocument/" + fileName);
        // Path path = Paths.get("C:/Users/pristine/Desktop/HRMSDocuments/Verification/" + fileName);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Create document and set values
        EmployeeVerificationDocument newDocument = new EmployeeVerificationDocument();
        newDocument.setFilePath(path.toString());
        newDocument.setVerificationStatus(false); // default status false
        newDocument.setLabel(label); // 👈 note spelling based on your model

        VerificationTicket verificationTicket = verificationTicketRepository.findById(verificationTicketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + verificationTicketId));
        PreRegistration preRegistration = preRegistrationRepository.findById(preRegistrationId)
                .orElseThrow(() -> new RuntimeException("PreRegistration not found with ID: " + preRegistrationId));

        newDocument.setVerificationTicket(verificationTicket);
        newDocument.setPreRegistration(preRegistration);

        employeeVerificationDocumentRepository.save(newDocument);

        return ResponseEntity.ok("Verification document uploaded successfully. Path: " + path);
    } catch (IOException e) {
        logger.error("IOException during document upload: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
    } catch (RuntimeException e) {
        logger.error("RuntimeException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
        logger.error("Unexpected error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
    }
}





@DeleteMapping("/delete/{id}")
public ResponseEntity<String> deleteDocumentById(@PathVariable Long id) {
    try {
        if (employeeVerificationDocumentRepository.existsById(id)) {
            employeeVerificationDocumentRepository.deleteById(id);
            return ResponseEntity.ok("Employee Verification Document deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Document not found with ID: " + id);
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to delete document: " + e.getMessage());
    }
}

@PutMapping("/updateVerificationStatus/{id}")
public ResponseEntity<String> updateVerificationStatus(
        @PathVariable Long id,
        @RequestParam boolean status) {

    try {
        EmployeeVerificationDocument document = employeeVerificationDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));

        document.setVerificationStatus(status);
        employeeVerificationDocumentRepository.save(document);

        return ResponseEntity.ok("Verification status updated successfully.");
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to update verification status: " + e.getMessage());
    }
}



@GetMapping("/view/Document/{verificationTicketId}")
public ResponseEntity<?> viewFilesByVerificationTicketId(@PathVariable Long verificationTicketId) {
    try {
        // Fetch documents matching the given verificationTicketId
        List<EmployeeVerificationDocument> verificationDocuments = employeeVerificationDocumentRepository.findAll().stream()
                .filter(doc -> doc.getVerificationTicket().getId().equals(verificationTicketId))
                .toList();

        if (verificationDocuments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No documents found for VerificationTicket ID: " + verificationTicketId);
        }

        // Prepare file details response
        List<Map<String, String>> fileDetails = new ArrayList<>();
        for (EmployeeVerificationDocument doc : verificationDocuments) {
            Path filePath = Paths.get(doc.getFilePath());
            if (Files.exists(filePath)) {
                Map<String, String> details = new HashMap<>();
                details.put("documentId", String.valueOf(doc.getId()));
                details.put("filePath", doc.getFilePath());
                details.put("fileName", filePath.getFileName().toString());
                details.put("downloadUrl", "/api/employeeVerificationDocument/view/" + doc.getId()); // assuming a download endpoint
                 // Add verificationStatus and label
                 details.put("verificationStatus", String.valueOf(doc.isVerificationStatus()));
                 details.put("label", doc.getLabel());
      
                fileDetails.add(details);
            }
        }

        if (fileDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No readable files found for VerificationTicket ID: " + verificationTicketId);
        }

        return ResponseEntity.ok(fileDetails);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while retrieving documents: " + e.getMessage());
    }
}






@GetMapping("/view/{id}")
public ResponseEntity<?> viewFile(@PathVariable Long id) {
    try {
        // Fetch the document by its ID
        EmployeeVerificationDocument employeeVerificationDocument = employeeVerificationDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));

        // Get the file path from the document
        Path filePath = Paths.get(employeeVerificationDocument.getFilePath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found at path: " + employeeVerificationDocument.getFilePath());
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

}
