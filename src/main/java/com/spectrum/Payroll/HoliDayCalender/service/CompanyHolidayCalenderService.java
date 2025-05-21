package com.spectrum.Payroll.HoliDayCalender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.Payroll.HoliDayCalender.model.CompanyHolidayCalender;
import com.spectrum.Payroll.HoliDayCalender.repository.CompanyHolidayCalenderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyHolidayCalenderService {

    @Autowired
    private CompanyHolidayCalenderRepository companyHolidayCalenderRepository;

    public List<CompanyHolidayCalender> saveAllHolidays(List<CompanyHolidayCalender> holidayList) {
        return companyHolidayCalenderRepository.saveAll(holidayList);
    }


    public List<CompanyHolidayCalender> getHolidaysByCompanyId(Long companyId) {
        return companyHolidayCalenderRepository.findByCompanyId(companyId);
    }

    public boolean deleteHolidayById(Long id) {
        Optional<CompanyHolidayCalender> holiday = companyHolidayCalenderRepository.findById(id);
        if (holiday.isPresent()) {
            companyHolidayCalenderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public List<CompanyHolidayCalender> getHolidaysByCompanyIdAndWorkStateCode(Long companyId, int workStateCode) {
        return companyHolidayCalenderRepository.findByCompanyIdAndWorkStateCode(companyId, workStateCode);
    }
}
