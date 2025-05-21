package com.spectrum.Document.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spectrum.Document.model.Document;
import com.spectrum.Document.repository.DocumentRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;


@RestController
@RequestMapping("/api/documents")

public class DocumentController {



    @Autowired
    private DocumentRepository documentRepository; // Add @Autowired

    @Autowired
    private EmployeeRepository employeeRepository; // Add @Autowired

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping("/{employeeId}/upload")
    public ResponseEntity<String> uploadDocument(
            @PathVariable Long employeeId, // Use the correct data type for the ID
            @RequestParam("file") MultipartFile file) {
        logger.info("Uploading file for employee ID: {}", employeeId);

        try {
            String fileName = file.getOriginalFilename();
            logger.info("File name received: {}", fileName);
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid file name");
            }

            // Path path = Paths.get("C:/Users/spectrum/Desktop/Employee/" + fileName);
            Path path = Paths.get("/home/ubuntu/DocumentHRMS/alldocument/" + fileName);
            // Path path = Paths.get("C:/Users/pristine/Desktop/HRMSDocuments/Employee/" + fileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Document document = new Document(path.toString());
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            document.setEmployee(employee);
            documentRepository.save(document);

            return ResponseEntity.ok("File uploaded successfully. Path: " + path);
        } catch (IOException e) {
            logger.error("IOException during file upload: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        } catch (RuntimeException e) {
            logger.error("RuntimeException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
    



    @GetMapping("/employee/{employeeId}")
public ResponseEntity<?> getDocumentsByEmployeeId(@PathVariable Long employeeId) {
    try {
        // Fetch documents by employeeId
        List<Document> documents = documentRepository.findAll()
                .stream()
                .filter(doc -> doc.getEmployee().getId().equals(employeeId))
                .collect(Collectors.toList());

        if (documents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No documents found for the given employee ID: " + employeeId);
        }

        return ResponseEntity.ok(documents);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while fetching documents: " + e.getMessage());
    }
}





@GetMapping("/view/{id}")
public ResponseEntity<?> viewFile(@PathVariable Long id) {
    try {
        // Fetch the document by its ID
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));

        // Get the file path from the document
        Path filePath = Paths.get(document.getFilePath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found at path: " + document.getFilePath());
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



@GetMapping("/view/employee/{employeeId}")
public ResponseEntity<?> viewFilesByEmployeeId(@PathVariable Long employeeId) {
    try {
        // Retrieve documents associated with the employee
        List<Document> documents = documentRepository.findAll().stream()
                .filter(doc -> doc.getEmployee().getId().equals(employeeId))
                .toList();

        if (documents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No documents found for employeeId: " + employeeId);
        }

        // Create a response with file details
        List<Map<String, String>> fileDetails = new ArrayList<>();
        for (Document doc : documents) {
            Path filePath = Paths.get(doc.getFilePath());
            if (Files.exists(filePath)) {
                Map<String, String> details = new HashMap<>();
                details.put("documentId", String.valueOf(doc.getId()));
                details.put("filePath", doc.getFilePath());
                details.put("fileName", filePath.getFileName().toString());
                details.put("downloadUrl", "/api/documents/view/" + doc.getId()); // Add download link
                fileDetails.add(details);
            }
        }

        if (fileDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No readable files found for employeeId: " + employeeId);
        }

        return ResponseEntity.ok(fileDetails);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while retrieving documents: " + e.getMessage());
    }








}



@DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteDocumentById(@PathVariable Long id) {
    try {
        // Check if the document exists
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));

        // Get the file path
        Path filePath = Paths.get(document.getFilePath());

        // Delete the file from the system if it exists
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found on disk for document ID: " + id);
        }

        // Delete the document entry from the database
        documentRepository.deleteById(id);

        return ResponseEntity.ok("Document and file deleted successfully with ID: " + id);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting the file from disk: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}







}


