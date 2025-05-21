package com.spectrum.Training.ReportFilterController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Training.ReportService.TrainingReportService;
import com.spectrum.Training.TrainingExcelExport.TrainingExcelExportService.ExcelExportService;
import com.spectrum.Training.repository.CertificationRepositoary;

@RestController
@RequestMapping("/api")
public class TraininReportsController {
    @Autowired
    private TrainingReportService trainingHRMSService;

    @GetMapping("/training-report-with-company-details")
    public List<Map<String, Object>> getTrainingReports() {
        return trainingHRMSService.getTrainingReportWithCompanyDetails();
    }

    @GetMapping("/training-report-companyTrainings")
    public Page<Map<String, Object>> getTrainingReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return trainingHRMSService.getTrainingReportWithCompanyDetails(pageable);
    }

    //////////////////////////////////////////////////////////////////////////////
    // 
    @GetMapping("/training-report-Certifications")
public Page<Map<String, Object>> getTrainingReportOFCertificate(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "date") String sortBy,
        @RequestParam(defaultValue = "desc") String direction
) {
    Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return trainingHRMSService.getTrainingReportWithCompanyDetails(pageable);
}
@GetMapping("/training-report-Certification")
public Page<Map<String, Object>> getTrainingReportOFCertificates(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "date") String sortBy,
        @RequestParam(defaultValue = "desc") String direction
) {
    Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return trainingHRMSService.getTrainingReportWithCompanyDetailsCertification(pageable);
}


    @GetMapping("/training-report-resultTrainings/report")
    public Page<Map<String, Object>> getResultTrainingReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "completionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return trainingHRMSService.getResultTrainingReport(pageable);
    }


    @GetMapping("/training-report-AssignTraining-report")
    public Page<Map<String, Object>> getTrainingReportAssignTraining(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "assignDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return trainingHRMSService.getTrainingReportWithCompanyDetails(pageable);
    }

    @GetMapping("/training-report-certificate")
    public ResponseEntity<Page<Map<String, Object>>> getReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Map<String, Object>> report = trainingHRMSService.getPaginatedCertificationReport(pageable);
        return ResponseEntity.ok(report);
    }
//     @Autowired
//    private  ExcelExportService excelExportService;
//     @GetMapping("/training-report/download")
//     public ResponseEntity<InputStreamResource> downloadTrainingReport(
//             @RequestParam(defaultValue = "0") int page,
//             @RequestParam(defaultValue = "10") int size,
//             @RequestParam(defaultValue = "assignDate") String sortBy,
//             @RequestParam(defaultValue = "desc") String sortDir) throws IOException {
    
//         Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//         Pageable pageable = PageRequest.of(page, size, sort);
    
//         Page<Map<String, Object>> reportPage = trainingHRMSService.getTrainingReportWithCompanyDetails(pageable);
//         List<Map<String, Object>> reportData = reportPage.getContent();
    
//         String[] headers = { "Training ID", "Heading", "Type", "Date", "Department" }; // You can customize
//         String[] keys = { "trainingId", "heading", "type", "date", "department" };     // Match with map keys
    
//         ByteArrayInputStream in = excelExportService.exportToExcel(reportData, headers, keys);
    
//         HttpHeaders headersResp = new HttpHeaders();
//         headersResp.add("Content-Disposition", "attachment; filename=training_report.xlsx");
    
//         return ResponseEntity.ok()
//                 .headers(headersResp)
//                 .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//                 .body(new InputStreamResource(in));
//     }
    
}
