package com.spectrum.ExitProcess.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.spectrum.ExitProcess.Model.KnowledgeTransfer;
import com.spectrum.ExitProcess.Model.KnowledgeTransferDocument;
import com.spectrum.ExitProcess.Repo.KTRepo;
import com.spectrum.ExitProcess.Repo.KnowledgeTransferDocumetRepo;


@RestController
@RequestMapping("/api/KnowledgeDocument")
public class KnowledgeTransferDocumentController {
    @Autowired
    private KnowledgeTransferDocumetRepo knowledgeTransferDocRepo;

    @Autowired
    private KTRepo knowledgeTransferRepo;
    
    @PostMapping("/{ktID}/upload")
    public ResponseEntity<String> uploadDocument(
            @PathVariable Long ktID, 
            @RequestParam("file") MultipartFile file) {
        
        // logger.info("Uploading file for Induction ID: {}", inductionId);
    
        try {
            // Validate file
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid file name");
            }
    
            // ✅ Save the file in the desired location
            // Path path = Paths.get("C:/Users/spectrum/Downloads",fileName);
            Path path = Paths.get("C:/hrmsdocs/ktdocs",fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(file.getInputStream());
            // ✅ Retrieve Induction from DB
            KnowledgeTransfer var1 = knowledgeTransferRepo.findById(ktID)
                    .orElseThrow(() -> new RuntimeException("Induction not found"));
    
            // ✅ Save DocumentInduction entity
            KnowledgeTransferDocument document = new KnowledgeTransferDocument(path.toString(),var1);
            document.setFileName(fileName);

            knowledgeTransferDocRepo.save(document);
            System.out.println("Doc:"+document);
    
            return ResponseEntity.ok("File uploaded successfully. Path: " + path);
        
        } catch (IOException e) {
            // logger.error("IOException during file upload: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        } catch (RuntimeException e) {
            // logger.error("RuntimeException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }


 @GetMapping("/DocumentTraining/{ktID}")
public ResponseEntity<?> getDocumentsByEmployeeId(@PathVariable Long ktID) {
    try {
        // Fetch documents by employeeId
        List<KnowledgeTransferDocument> documents = knowledgeTransferDocRepo.findAll();
              

        if (documents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No documents found for the given employee ID: " + ktID);
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
        KnowledgeTransferDocument document = knowledgeTransferDocRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));

        // Get the file path from the document
        Path filePath = Paths.get(document.getFilePath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found at path: " + document.getFilePath());
        }

        // Load the file as a resource
        Resource resource =  new UrlResource(filePath.toUri());
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
@GetMapping("/view/Offboarding/{KTId}")
public ResponseEntity<?> viewFilesByInductionIdList(@PathVariable Long KTId) {
    try {
        List<KnowledgeTransferDocument> documents = knowledgeTransferDocRepo.findByKTtransfer_Id(KTId);

        if (documents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No documents found for inductionId: " + KTId);
        }

        List<Map<String, String>> fileDetails = new ArrayList<>();
        for (KnowledgeTransferDocument doc : documents) {
            Path filePath = Paths.get(doc.getFilePath());
            if (Files.exists(filePath)) {
                Map<String, String> details = new HashMap<>();
                details.put("documentId", String.valueOf(doc.getId()));
                details.put("filePath", doc.getFilePath());
                details.put("fileName", filePath.getFileName().toString());
                details.put("downloadUrl", "/api/documents/view/" + doc.getId()); 
                fileDetails.add(details);
            }
        }

        if (fileDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No readable files found for TrainingId: " + KTId);
        }

        return ResponseEntity.ok(fileDetails);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while retrieving documents: " + e.getMessage());
    }
}


}
