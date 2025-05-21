package com.spectrum.service;
 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
 
@Service
public class EmployeeService {
 
	@Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private EmailService emailService;

	public Optional<Employee> getById(long id) {
        return employeeRepository.findById(id);
    }

 
//	public Employee saveEmployee(Employee emp) {
//		return repo.save(emp);
//	}
//	
	public List<Employee> getByFirstName(String firstName) {
        return employeeRepository.findByFirstName(firstName);
    }
    public List<Employee> getByLastName(String lastName) {
        return employeeRepository.findByLastName(lastName);
    }
 
    public Employee updateById(long employeeId, Employee newEmployeeData) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setEmployeeId(newEmployeeData.getEmployeeId());

            existingEmployee.setId(newEmployeeData.getId());
            existingEmployee.setFirstName(newEmployeeData.getFirstName());
            existingEmployee.setLastName(newEmployeeData.getLastName());
            existingEmployee.setMiddleName(newEmployeeData.getMiddleName());
            existingEmployee.setMotherName(newEmployeeData.getMotherName());
            existingEmployee.setContactNo(newEmployeeData.getContactNo());
            existingEmployee.setEmail(newEmployeeData.getEmail());
            existingEmployee.setGender(newEmployeeData.getGender());
            existingEmployee.setNationality(newEmployeeData.getNationality());
            existingEmployee.setDesignation(newEmployeeData.getDesignation());
            existingEmployee.setDepartment(newEmployeeData.getDepartment());
            existingEmployee.setExperience(newEmployeeData.getExperience());
            existingEmployee.setPanNo(newEmployeeData.getPanNo());
            existingEmployee.setAlternateEmail(newEmployeeData.getAlternateEmail());
            existingEmployee.setAdhaarNo(newEmployeeData.getAdhaarNo());
            existingEmployee.setContactNoCountryCode(newEmployeeData.getContactNoCountryCode());
            existingEmployee.setAlternateContactNo(newEmployeeData.getAlternateContactNo());
            existingEmployee.setAlternateContactNoCountryCode(newEmployeeData.getAlternateContactNoCountryCode());
            // existingEmployee.setAbscondDate(newEmployeeData.getAbscondDate());
            existingEmployee.setJoiningDate(newEmployeeData.getJoiningDate());
            // existingEmployee.setExitDate(newEmployeeData.getExitDate());
            existingEmployee.setPresence(newEmployeeData.isPresence());
            // existingEmployee.setResign(newEmployeeData.isResign());
            existingEmployee.setPriorId(newEmployeeData.getPriorId());
            existingEmployee.setEmployeeType(newEmployeeData.getEmployeeType());
            existingEmployee.setCurrentStreet(newEmployeeData.getCurrentStreet());
            existingEmployee.setCurrentCity(newEmployeeData.getCurrentCity());
            existingEmployee.setCurrentState(newEmployeeData.getCurrentState());
            existingEmployee.setCurrentPostelcode(newEmployeeData.getCurrentPostelcode());
            existingEmployee.setCurrentCountry(newEmployeeData.getCurrentCountry());
            existingEmployee.setPermanentStreet(newEmployeeData.getPermanentStreet());
            existingEmployee.setPermanentCity(newEmployeeData.getPermanentCity());
            existingEmployee.setPermanentState(newEmployeeData.getPermanentState());
            existingEmployee.setPermanentPostelcode(newEmployeeData.getPermanentPostelcode());
            existingEmployee.setPermanentCountry(newEmployeeData.getPermanentCountry());
            existingEmployee.setEducations(newEmployeeData.getEducations());
            existingEmployee.setGenerateProjects(newEmployeeData.getGenerateProjects());
            existingEmployee.setPermanentHouseNo(newEmployeeData.getPermanentHouseNo());
            existingEmployee.setCurrentHouseNo(newEmployeeData.getCurrentHouseNo());
            existingEmployee.setDivision(newEmployeeData.getDivision());
            // Update other fields as needed
 
            return employeeRepository.save(existingEmployee);
        } else {
            return null; // Or you can throw an exception indicating that the employee with the given ID was not found
        }
    }
 
    // public Employee saveEmployee(Employee employee) {
    // return repo.save(employee);
    // }

    public Employee saveEmployee(Long companyId, Employee employee) throws Exception {
        // Retrieve the company using the companyId
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new Exception("Company not found with id: " + companyId));

        // Set the companyRegistration for the employee
        employee.setCompanyRegistration(company);

        // Check if the employeeId is already used in the same company
        boolean exists = employeeRepository.existsByEmployeeIdAndCompanyRegistration(
                employee.getEmployeeId(), company);

        if (exists) {
            throw new Exception("Employee ID already exists for this company.");
        }

        return employeeRepository.save(employee);
    }

    public Employee saveEmployeeAndSendEmail(Long companyId, Employee employee) throws Exception {
        // Retrieve the company using the companyId
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new Exception("Company not found with id: " + companyId));

        // Set the companyRegistration for the employee
        employee.setCompanyRegistration(company);

        // Check if the employeeId is already used in the same company
        boolean exists = employeeRepository.existsByEmployeeIdAndCompanyRegistration(
                employee.getEmployeeId(), company);

        if (exists) {
            throw new Exception("Employee ID already exists for this company.");
        }

        // Save the employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Send an email to the employee after successful save
        try {
            sendEmailToEmployee(savedEmployee);
        } catch (Exception e) {
            // Handle email sending failure
            throw new Exception("Employee saved, but failed to send email: " + e.getMessage());
        }

        return savedEmployee;
    }

    private void sendEmailToEmployee(Employee employee) throws Exception {
        // Subject of the email
        String subject = "Welcome to Pristine IT Code! Your Employee ID Has Been Created";
    
        // Email body message
        String message = "Dear " + employee.getFirstName() + ",\n\n" +
                "A warm welcome to " + employee.getCompanyRegistration().getCompanyName() + "! " +
                "We are thrilled to have you on board and are excited to have you as a part of our team.\n\n" +
                "As part of our onboarding process, we have created your Employee ID in our HRMS system. " +
                "This ID will serve as your unique identifier within the organization and will provide you with access to " +
                "various HR-related services and tools.\n\n" +
                "Your Employee ID Details:\n" +
                "Employee Name: " + employee.getFirstName() + " " + employee.getMiddleName() + " " + employee.getLastName() + "\n" +
                "Employee ID: " + employee.getEmployeeId() + "\n" +
                "Department: " + employee.getDepartment() + "\n" +
                "Date of Joining: " + employee.getJoiningDate() + "\n\n" +
                "Your Employee ID is important for accessing company systems and services, including:\n" +
                "- The HRMS portal for attendance, leave management, and personal records.\n" +
                "- Internal systems and resources.\n\n" +
                "Please note that your Employee ID and HRMS access are confidential and should not be shared with anyone. " +
                "If you have any issues with your login credentials, please contact our HR department for assistance.\n\n" +
                "Once again, welcome to " + employee.getCompanyRegistration().getCompanyName() + "! " +
                "We look forward to seeing your contributions and growth within the organization.\n\n" +
                "Best regards,\nHR Department\n"+ employee.getCompanyRegistration().getCompanyName()+"";
    
        // Send the email using the email service
        emailService.sendEmail(employee.getEmail(), subject, message);
    }
    
    public Page<Employee> getActiveEmployeesByCompanyId(Long companyId, Pageable pageable) {
        return employeeRepository.findByPresenceAndCompanyRegistration_Id(true, companyId, pageable);
    }


    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }



    public Long getActiveEmployeeCountByCompanyId(Long companyId) {
        return employeeRepository.countActiveEmployeesByCompanyId(companyId);
    }
}
