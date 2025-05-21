package com.spectrum.Payroll.HoliDayCalender.service;

import com.spectrum.Payroll.HoliDayCalender.model.CompanyDayOffConfig;
import com.spectrum.Payroll.HoliDayCalender.repository.CompanyDayOffConfigRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompanyDayOffConfigService {

    private final CompanyDayOffConfigRepository companyDayOffConfigRepository;
    private final CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    public CompanyDayOffConfigService(CompanyDayOffConfigRepository companyDayOffConfigRepository,
            CompanyRegistrationRepository companyRegistrationRepository) {
        this.companyDayOffConfigRepository = companyDayOffConfigRepository;
        this.companyRegistrationRepository = companyRegistrationRepository;
    }

    public void saveMultiple(Long companyId, List<CompanyDayOffConfig> companyDayOffConfigs) {
        // Fetch the company by companyId
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company with ID " + companyId + " not found"));

        // Set the company for each CompanyDayOffConfig and save
        for (CompanyDayOffConfig config : companyDayOffConfigs) {
            config.setCompany(company);
        }

        // Save all configurations in a batch
        companyDayOffConfigRepository.saveAll(companyDayOffConfigs);

    }

    public List<CompanyDayOffConfig> findByCompanyId(Long companyId) {
        return companyDayOffConfigRepository.findByCompanyId(companyId);
    }

    public void deleteById(Long id) {
        if (!companyDayOffConfigRepository.existsById(id)) {
            throw new IllegalArgumentException("Configuration with ID " + id + " not found");
        }
        companyDayOffConfigRepository.deleteById(id);
    }





    public List<LocalDate> findDistinctHolidayDatesByCompanyId(Long companyId) {
        return companyDayOffConfigRepository.findDistinctHolidayDatesByCompanyId(companyId);
    }
    


    public void deleteByCategoryCodeAndCompanyId(int workCategoryCode, Long companyId) {
        companyDayOffConfigRepository.deleteByCategoryCodeAndCompanyId(workCategoryCode, companyId);
    }


    public List<CompanyDayOffConfig> findByCompanyIdAndCategoryCode(Long companyId, int workCategoryCode) {
        return companyDayOffConfigRepository.findByCompanyIdAndWorkCategoryCode(companyId, workCategoryCode);
    }
    
}
