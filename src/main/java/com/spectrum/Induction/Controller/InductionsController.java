package com.spectrum.Induction.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Induction.Model.Inductions;
import com.spectrum.Induction.Service.InductionsService;

@RestController
@RequestMapping("/api/inductions")
public class InductionsController {
     @Autowired
    private InductionsService inductionsService;

    // ✅ Save Induction
    @PostMapping("post")
    public ResponseEntity<Inductions> createInduction(@RequestBody Inductions inductions) {
        Inductions savedInduction = inductionsService.saveInduction(inductions);
        return ResponseEntity.ok(savedInduction);
    }

    // ✅ Get All Inductions
    @GetMapping("getall")
    public ResponseEntity<List<Inductions>> getAllInductions() {
        List<Inductions> inductionsList = inductionsService.getAllInductions();
        return ResponseEntity.ok(inductionsList);
    }

    // ✅ Get Induction by ID
    @GetMapping("get/{id}")
    public ResponseEntity<Inductions> getInductionById(@PathVariable Long id) {
        Inductions induction = inductionsService.getInductionById(id);
        return ResponseEntity.ok(induction);
    }

    // ✅ Update Induction
    @PutMapping("update/{id}")
    public ResponseEntity<Inductions> updateInduction(@PathVariable Long id, @RequestBody Inductions updatedInduction) {
        Inductions induction = inductionsService.updateInduction(id, updatedInduction);
        return ResponseEntity.ok(induction);
    }

    // ✅ Delete Induction
    @DeleteMapping("/del/{id}")
    public ResponseEntity<String> deleteInduction(@PathVariable Long id) {
        inductionsService.deleteInduction(id);
        return ResponseEntity.ok("Induction deleted successfully");
    }  

    /////////////////////////////////////////////////////////////////////////////////////
    // api related to company and induction 
    @PostMapping("/save/{companyId}/{createdBy}")
    public Inductions saveInduction(@PathVariable Long companyId, @PathVariable Long createdBy,@RequestBody Inductions inductions) {
        return inductionsService.saveInduction(companyId, inductions,createdBy);
    }


     @GetMapping("/byYearAndCompany")
    public List<Inductions> getInductionsByYearAndCompany(@RequestParam int year, @RequestParam Long companyId) {
        return inductionsService.getInductionsByYearAndCompany(year, companyId);
    }


    @GetMapping("/by-status")
    public ResponseEntity<List<Inductions>> getInductionsByCompanyAndStatus(
            @RequestParam Long companyId, 
            @RequestParam boolean status) {

    List<Inductions> inductions = inductionsService.getInductionsByCompanyAndStatus(companyId, status);
     
        
        return ResponseEntity.ok(inductions);
    }


    // @GetMapping("/by-year-region")
    // public ResponseEntity<List<Inductions>> getInductionsByYearAndRegion(
    //         @RequestParam int year,
    //         @RequestParam String region) {
        
    //     List<Inductions> inductions = inductionsService.getInductionsByYearAndRegion(year, region);
    //     return ResponseEntity.ok(inductions);
    // }


    @GetMapping("/by-year-region")
    public ResponseEntity<List<Inductions>> searchInductions(
            @RequestParam int year,
            @RequestParam Long region,
            @RequestParam Long companyId) {
        
        List<Inductions> result = inductionsService.getInductionsByYearRegionAndCompany(year, region, companyId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<Inductions>> searchInductions(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String heading,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        PageRequest pageable = PageRequest.of(page, size, sort);
        
        Page<Inductions> result = inductionsService.searchInductionRecords(
            year, description, heading, companyId, regionId, departmentId, pageable);
        
        return ResponseEntity.ok(result);
    }


}
