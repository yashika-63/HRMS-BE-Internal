package com.spectrum.ExitProcess.Service;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.spectrum.ExitProcess.Model.OffBoarding;
import com.spectrum.ExitProcess.Repo.OffBoardingRepo;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.util.StringUtils;
@Service
public class OffboardingService {

    @Autowired
    private OffBoardingRepo offBoardingRepo;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;


    @Autowired
    private EmployeeRepository employeeRepository;

     @Autowired
    private EmailService emailService;

    public List<OffBoarding> getAllOffBoardings() {
        return offBoardingRepo.findAll();
    }

    public OffBoarding getOffBoardingById(Long id) {
        Optional<OffBoarding> offBoarding = offBoardingRepo.findById(id);
        return offBoarding.orElseThrow(()-> new RuntimeException("Id not found"));
    }

    public OffBoarding createOffBoarding(OffBoarding offBoarding) {
        return offBoardingRepo.save(offBoarding);
    }

    public OffBoarding updateOffBoarding(Long id, OffBoarding offBoarding) {
        if (offBoardingRepo.existsById(id)) {
            offBoarding.setId(id);

            return offBoardingRepo.save(offBoarding);
        }
        return null;
    }

    public void deleteOffBoarding(Long id) {
        offBoardingRepo.deleteById(id);
    }

    public OffBoarding saveOffBoarding(OffBoarding offBoarding, Long companyId, Long employeeId, Long managerId, Long hrId) {
        if(companyId==null||employeeId==null||managerId==null||hrId==null){
            throw new RuntimeException("Some of the Parameter is null");
        }
// came  here earlier
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee manager = employeeRepository.findById(managerId)
            .orElseThrow(() -> new RuntimeException("Manager not found"));
        Employee hr = employeeRepository.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

            boolean exist=offBoardingRepo.existsByEmployeeIdAndAppliedTrue(employeeId);
            if(exist){
                throw new RuntimeException("Employee Has Applied");
            }
        

        offBoarding.setCompany(company);
        offBoarding.setEmployee(employee);
        offBoarding.setEmployees(manager);
        offBoarding.setEmployeeOne(hr);
        offBoarding.setCompletionStatus(false);
        offBoarding.setApplied(true);
        sendResignationNotifications(employee, manager, hr);
        return offBoardingRepo.save(offBoarding);
    }
    public void sendResignationNotifications(Employee employee, Employee manager, Employee hr) {
        // Format employee name safely
        String employeeName = formatEmployeeName(employee);
        
        // Email to HR
        String hrSubject = "Employee Resignation Notification - " + employeeName;
        String hrContent = String.format(
            "Dear HR Team,\n\n" +
            "This is to inform you that %s (Employee ID: %s) has submitted their resignation.\n" +
            "Reporting Manager: %s\n\n" +
            "Please initiate the offboarding process.\n\n" +
            "Regards,\n" +
            "System Notification",
            employeeName,
            employee.getEmployeeId(),
            formatEmployeeName(manager)
        );
        
        emailService.sendManagerResignationNotification(hr.getEmail(), hrSubject, hrContent);
        
        // Email to Manager
        String managerSubject = "Team Member Resignation - " + employeeName;
        String managerContent = String.format(
            "Dear %s,\n\n" +
            "This is to inform you that your team member %s (Employee ID: %s) has submitted their resignation.\n\n" +
            "Please coordinate with HR for the offboarding process.\n\n" +
            "Regards,\n" +
            "System Notification",
            formatEmployeeName(manager),
            employeeName,
            employee.getEmployeeId()
        );
        
        emailService.sendEmail(manager.getEmail(), managerSubject, managerContent);
    }

    private String formatEmployeeName(Employee employee) {
        if (employee == null) {
            return "";
        }
        
        StringBuilder name = new StringBuilder();
        
        if (employee.getFirstName() != null) {
            name.append(employee.getFirstName()).append(" ");
        }
        if (employee.getMiddleName() != null) {
            name.append(employee.getMiddleName()).append(" ");
        }
        if (employee.getLastName() != null) {
            name.append(employee.getLastName());
        }
        
        String result = name.toString().trim();
        return result.isEmpty() ? "Employee ID: " + employee.getEmployeeId() : result;
    }

    public List<OffBoarding> getByCompletionStatusAndCompany(boolean completionStatus, Long companyId) {
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
        .orElseThrow(() -> new RuntimeException("Company not found"));

        return offBoardingRepo.findByCompletionStatusAndCompanyId(completionStatus, companyId);
    }
 
//  public OffBoarding getByNameAndempId(String name,Long empId)
//  {

//     return offBoardingRepo.findBynameAndEmployee_id(name, empId);
//  }
public List<OffBoarding> searchOffBoardings(String employeeId, String employeeName) {
    boolean hasEmployeeId = StringUtils.hasText(employeeId);
    boolean hasEmployeeName = StringUtils.hasText(employeeName);

    if (hasEmployeeId && hasEmployeeName) {
        return offBoardingRepo.findByEmployeeEmployeeIdAndNameContainingIgnoreCase(employeeId, employeeName);
    } else if (hasEmployeeId) {
        return offBoardingRepo.findByEmployeeEmployeeId(employeeId);
    } else if (hasEmployeeName) {
        return offBoardingRepo.findByEmployeeNameContainingIgnoreCase(employeeName);
    } else {
        return offBoardingRepo.findAll();
    }
}


public List<OffBoarding> getByStatusAndCompanyId(Boolean status, Long companyId) {
    if(companyId==null){
        throw new RuntimeException("companyId is null");
    }
    CompanyRegistration company = companyRegistrationRepository.findById(companyId)
    .orElseThrow(() -> new RuntimeException("Company not found"));
    if(status==null){
        throw new RuntimeException("Status is null");
    }

    return offBoardingRepo.getByStatusAndCompanyId(status, companyId);
   }


//    public List<OffBoarding> getByStatusCompanyIdAndManagerId(Boolean status, Long companyId, Long managerId) {
//     if (companyId == null ||status == null||managerId == null ) {
//         throw new RuntimeException("Some of the parameter is missing");
//     }

//     CompanyRegistration company = companyRegistrationRepository.findById(companyId)
//         .orElseThrow(() -> new RuntimeException("Company not found"));

   
//     Employee manager = employeeRepository.findById(managerId)
//     .orElseThrow(() -> new RuntimeException("Manager not found"));

//     return offBoardingRepo.findByStatusAndCompanyIdAndEmployees_Id(status, companyId, managerId);
// }

// public List<OffBoarding> searchOffBoarding(Long deptId, Boolean completionStatus, Integer year) {
//     return offBoardingRepo.searchOffBoarding(deptId, completionStatus, year);
// }
    
// public Page<OffBoarding> searchOffBoarding(Long deptId, Boolean completionStatus, Integer year, Pageable pageable) {
//     return offBoardingRepo.searchOffBoarding(deptId, completionStatus, year, pageable);
// }




public OffBoarding completeOffBoarding(Long offBoardingId, String completedBy) throws MessagingException {
        Optional<OffBoarding> optionalOffBoarding = offBoardingRepo.findById(offBoardingId);

        if (optionalOffBoarding.isEmpty()) {
            throw new RuntimeException("OffBoarding record not found for id: " + offBoardingId);
        }

        OffBoarding offBoarding = optionalOffBoarding.get();
        offBoarding.setCompletionStatus(true);
        offBoarding.setCompletedBy(completedBy);

        OffBoarding updated = offBoardingRepo.save(offBoarding);

        // Notify employee and reporting manager
        Employee employee = updated.getEmployee();
        Employee manager = updated.getEmployees(); // reporting manager

        String subject = "Offboarding Completed for " + employee.getFirstName();
        String message = String.format(
            "Hello,\n\nThe offboarding process for %s has been completed by %s on %s.\n\nDetails:\n- Department: %s\n- Last Working Date: %s\n- Reason: %s\n\nRegards,\nHR",
            employee.getFirstName(),
            completedBy,
            updated.getDate(),
            updated.getDepartment(),
            updated.getLastWorkingDate(),
            updated.getReason()
        );

        emailService.sendEmail(employee.getEmail(), subject, message);
        emailService.sendEmail(manager.getEmail(), subject, message);

        return updated;
    }
    public Page<OffBoarding> getOffBoardings(Boolean status, Long companyId, int page, int size) {
            // if(companyId==null || status==null ){
            //     throw new RuntimeException("Some Parameter is null");
            // }
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
        .orElseThrow(() -> new RuntimeException("Company not found"));
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")); // Assuming 'id' is the field to sort by
    return offBoardingRepo.findByStatusAndCompanyId(status, companyId, pageable);
}

public Page<OffBoarding> getByStatusCompanyIdAndManagerId(Boolean status, Long companyId, Long managerId, int page, int size) {
    if (companyId == null || status == null || managerId == null) {
        throw new RuntimeException("Some of the parameters are missing");
    }

    CompanyRegistration company = companyRegistrationRepository.findById(companyId)
        .orElseThrow(() -> new RuntimeException("Company not found"));

    Employee manager = employeeRepository.findById(managerId)
        .orElseThrow(() -> new RuntimeException("Manager not found"));

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
    return offBoardingRepo.findByStatusAndCompanyIdAndEmployees_Id(status, companyId, managerId, pageable);
}
// public Page<OffBoarding> searchOffBoarding(Long deptId, Boolean completionStatus, Integer year, Pageable pageable) {
//     return offBoardingRepo.searchOffBoardings(deptId, completionStatus, year, pageable);
// }
public Page<OffBoarding> getByStatusOrCompanyId(Boolean status, Long companyId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return offBoardingRepo.findByStatusOrCompanyId(status, companyId, pageable);
}

public Page<OffBoarding> searchOffBoarding(Long deptId, Boolean completionStatus, Integer year, Pageable pageable) {
    return offBoardingRepo.searchOffBoardings(deptId, completionStatus, year, pageable);
}
}
