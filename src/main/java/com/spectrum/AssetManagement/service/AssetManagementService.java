package com.spectrum.AssetManagement.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.AssetManagement.model.AssetManagement;
import com.spectrum.AssetManagement.model.AssetManagementDetails;
import com.spectrum.AssetManagement.model.AssetManagementDetailsUpdateDTO;
import com.spectrum.AssetManagement.repository.AssetManagementDetailsRepository;
import com.spectrum.AssetManagement.repository.AssetManagementRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AssetManagementService {

    @Autowired
    private AssetManagementRepository assetManagementRepository;

    @Autowired
    private AssetManagementDetailsRepository assetManagementDetailsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public String saveAssetWithDetails(Long employeeId, List<AssetManagementDetails> details) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        AssetManagement assetManagement = new AssetManagement();
        assetManagement.setEmployee(employee);
        assetManagement.setEmployeeAction(false); // or true based on requirement
        assetManagement.setSentForEmployeeAction(false);
        assetManagement = assetManagementRepository.save(assetManagement);

        for (AssetManagementDetails detail : details) {
            detail.setAssetManagement(assetManagement);
        }

        assetManagementDetailsRepository.saveAll(details);
        return "Asset saved successfully.";
    }


    public List<AssetManagement> getByEmployeeIdAndEmployeeAction(Long employeeId, boolean employeeAction) {
        return assetManagementRepository.findByEmployee_IdAndEmployeeAction(employeeId, employeeAction);
    }

    






     @Autowired
    private EmailService emailService;

    public AssetManagement updateSentForEmployeeAction(Long assetId) {
        Optional<AssetManagement> optionalAsset = assetManagementRepository.findById(assetId);
        if (optionalAsset.isPresent()) {
            AssetManagement asset = optionalAsset.get();
            asset.setSentForEmployeeAction(true);
            AssetManagement updated = assetManagementRepository.save(asset);

            String email = asset.getEmployee().getEmail();
            String subject = "Action Required: Asset Management Request";
            String body = "Dear " + asset.getEmployee().getFirstName() + ",\n\n" +
                    "You have been requested to take action on an asset management record. " +
                    "Please complete your response within **2 days**.\n\n" +
                    "Regards,\nHR/IT Team";

            emailService.sendEmail(email, subject, body);

            return updated;
        } else {
            throw new RuntimeException("AssetManagement record not found with id: " + assetId);
        }
    }






    public List<AssetManagement> getByEmployeeIdAndSentForActionTrue(Long employeeId) {
        return assetManagementRepository.findByEmployee_IdAndSentForEmployeeActionTrue(employeeId);
    }


    public String updateAssetAction(Long id, String actiontakenBy) {
        Optional<AssetManagement> optionalAsset = assetManagementRepository.findById(id);
    
        if (optionalAsset.isPresent()) {
            AssetManagement asset = optionalAsset.get();
    
            // Only update selected fields
            asset.setSentForEmployeeAction(false);
            asset.setActiontakenBy(actiontakenBy);
            asset.setEmployeeAction(true);
    
            assetManagementRepository.save(asset);
            return "Asset updated successfully.";
        } else {
            return "Asset not found with id: " + id;
        }
    }
    




    public void updateDescriptions(List<AssetManagementDetailsUpdateDTO> updateList, Long employeeId) {
        for (AssetManagementDetailsUpdateDTO dto : updateList) {
            AssetManagementDetails detail = assetManagementDetailsRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Detail not found: " + dto.getId()));
    
            AssetManagement asset = detail.getAssetManagement();
    
            // Check if the AssetManagement is assigned to this employee
            if (asset.getEmployee() == null || !asset.getEmployee().getId().equals(employeeId)) {
                throw new SecurityException("Asset does not belong to employee ID: " + employeeId);
            }
    
            // Update description
            detail.setDescription(dto.getDescription());
            assetManagementDetailsRepository.save(detail);
    
            // Update AssetManagement flags
            asset.setSentForEmployeeAction(false);
            asset.setEmployeeAction(true);
            assetManagementRepository.save(asset);
        }
    }
    

    public List<AssetManagementDetails> getByEmployeeId(Long employeeId) {
        return assetManagementDetailsRepository.findByEmployeeId(employeeId);
    }
    

    

    public List<AssetManagement> getByEmployeeIdWithDetails(Long employeeId) {
        return assetManagementRepository.findByEmployeeIdWithDetails(employeeId);
    }
    
    @Transactional
public void updateSubmittedToTrue(Long id) {

    AssetManagementDetails asset = assetManagementDetailsRepository.findById(id).orElseThrow(()-> new RuntimeException("Asset Not found for particular Employee"));
    if (asset.isSubmitted()) {
        throw new RuntimeException("This asset has already been submitted.");
    }

    asset.setSubmitted(true);
    assetManagementDetailsRepository.save(asset); // Explicit save to persist the change
}
    
}
