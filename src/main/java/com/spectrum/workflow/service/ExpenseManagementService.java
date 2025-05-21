package com.spectrum.workflow.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.spectrum.workflow.model.ExpenseManagement;
import com.spectrum.workflow.model.ExpenseDetails;
import com.spectrum.workflow.model.WorkflowDetail;
import com.spectrum.workflow.repository.ExpenseManagementRepository;
import com.spectrum.workflow.repository.ExpenseDetailsRepository;
import com.spectrum.workflow.repository.WorkflowDetailRepository;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

@Service
public class ExpenseManagementService {

    @Autowired
    private ExpenseManagementRepository expenseManagementRepository;

    @Autowired
    private ExpenseDetailsRepository expenseDetailsRepository;

    @Autowired
    private WorkflowDetailRepository workflowDetailRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

   
    ///////// save
    public ExpenseManagement saveExpenseManagement(long employeeId, long companyId, long workflowMainId,
            ExpenseManagement expenseManagement) {
        // Fetch the necessary entities
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        CompanyRegistration companyRegistration = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("CompanyRegistration not found"));
        WorkflowDetail firstDetail = workflowDetailRepository.findFirstByWorkflowMainIdOrderByIdAsc(workflowMainId);

        if (firstDetail != null) {
            // Set predefined values
            expenseManagement.setEmployee(employee);
            expenseManagement.setCompanyRegistration(companyRegistration); // Assuming there's a field for
                                                                           // CompanyRegistration
            expenseManagement.setWorkflowDivision(firstDetail.getWorkflowToDivision());
            expenseManagement.setWorkflowDepartment(firstDetail.getWorkflowToDepartment());
            expenseManagement.setWorkflowRole(firstDetail.getWorkflowNextRole());
            expenseManagement.setWorkflowMain(firstDetail.getWorkflowMain());
            expenseManagement.setRequestStatus("PENDING");
            // Save ExpenseManagement entity
            ExpenseManagement savedExpenseManagement = expenseManagementRepository.save(expenseManagement);

            // Save ExpenseDetails entities
            for (ExpenseDetails detail : expenseManagement.getExpenseDetails()) {
                detail.setExpenseManagement(savedExpenseManagement);
                expenseDetailsRepository.save(detail);
            }

            return savedExpenseManagement;
        } else {
            throw new RuntimeException("No workflow details found for the given workflowMainId.");
        }
    }

    public List<ExpenseManagement> findByCriteria(long companyId, String workflowDivision, String workflowDepartment,
            String workflowRole) {

        return expenseManagementRepository
                .findByCompanyRegistrationIdAndWorkflowDivisionAndWorkflowDepartmentAndWorkflowRole(
                        companyId, workflowDivision, workflowDepartment, workflowRole);
    }


    public ExpenseManagement updateWorkflowDetails(long expenseManagementId, long companyId, long workflowMainId,
            String workflowDivision, String workflowDepartment, String workflowRole) {

        // Retrieve the ExpenseManagement entity based on the provided ID and company ID
        Optional<ExpenseManagement> expenseManagementOpt = expenseManagementRepository
                .findByIdAndCompanyRegistrationId(expenseManagementId, companyId);
        if (!expenseManagementOpt.isPresent()) {
            throw new RuntimeException("ExpenseManagement not found");
        }

        ExpenseManagement expenseManagement = expenseManagementOpt.get();

        // Retrieve the WorkflowDetail based on the current workflow division,
        // department, and role
        Optional<WorkflowDetail> workflowDetailOpt = workflowDetailRepository
                .findByWorkflowMainIdAndWorkflowFromDivisionAndWorkflowFromDepartmentAndWorkflowPreviousRole(
                        workflowMainId, workflowDivision, workflowDepartment, workflowRole);

        if (!workflowDetailOpt.isPresent()) {
            // If no matching WorkflowDetail is found, mark the request as approved and
            // reset workflow details
            expenseManagement.setRequestStatus("APPROVED");

            // Retrieve the first WorkflowDetail from the associated WorkflowMain
            WorkflowDetail firstWorkflowDetail = workflowDetailRepository
                    .findFirstByWorkflowMainIdOrderByIdAsc(expenseManagement.getWorkflowMain().getId());

            if (firstWorkflowDetail != null) {
                expenseManagement.setWorkflowDivision(firstWorkflowDetail.getWorkflowToDivision());
                expenseManagement.setWorkflowDepartment(firstWorkflowDetail.getWorkflowToDepartment());
                expenseManagement.setWorkflowRole(firstWorkflowDetail.getWorkflowNextRole());
            } else {
                throw new RuntimeException("No initial workflow details found for the given workflowMainId.");
            }
        } else {
            // If a matching WorkflowDetail is found, update the expense management's
            // workflow details
            WorkflowDetail workflowDetail = workflowDetailOpt.get();
            expenseManagement.setWorkflowDivision(workflowDetail.getWorkflowToDivision());
            expenseManagement.setWorkflowDepartment(workflowDetail.getWorkflowToDepartment());
            expenseManagement.setWorkflowRole(workflowDetail.getWorkflowNextRole());
        }

        // Save and return the updated ExpenseManagement entity
        return expenseManagementRepository.save(expenseManagement);
    }

    public ExpenseManagement declineWorkflowDetails(long expenseManagementId, long companyId, long employeeId) {
        Optional<ExpenseManagement> ExpenseManagementOpt = expenseManagementRepository
                .findByIdAndCompanyRegistrationIdAndEmployeeId(expenseManagementId, companyId, employeeId);
        if (!ExpenseManagementOpt.isPresent()) {
            throw new RuntimeException("Expense not found");
        }

        ExpenseManagement expenseManagement = ExpenseManagementOpt.get();
        expenseManagement.setRequestStatus("REJECTED");
        Optional<Employee> employeeOpt = employeeRepository.findByIdAndCompanyRegistration_Id(employeeId, companyId);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            expenseManagement.setWorkflowDivision(employee.getDivision());
            expenseManagement.setWorkflowDepartment(employee.getDepartment());
            expenseManagement.setWorkflowRole(employee.getRole());
        } else {
            throw new RuntimeException("Employee not found");
        }

        return expenseManagementRepository.save(expenseManagement);
    }

    public List<ExpenseManagement> findByEmployeeId(Long employeeId) {
        return expenseManagementRepository.findByEmployeeId(employeeId);
    }

    public List<ExpenseManagement> findTop10ByEmployeeIdOrderByIdDesc(Long employeeId) {
        return expenseManagementRepository.findTop10ByEmployeeIdOrderByIdDesc(employeeId);
    }

    public ExpenseManagement updateExpenseManagement(Long id, ExpenseManagement updatedExpenseManagement) {
        Optional<ExpenseManagement> existingExpenseManagementOptional = expenseManagementRepository.findById(id);

        if (existingExpenseManagementOptional.isPresent()) {
            ExpenseManagement existingExpenseManagement = existingExpenseManagementOptional.get();

            // Update fields
            existingExpenseManagement.setExpenseFromDate(updatedExpenseManagement.getExpenseFromDate());
            existingExpenseManagement.setExpenseTillDate(updatedExpenseManagement.getExpenseTillDate());
            existingExpenseManagement.setExpensePurpose(updatedExpenseManagement.getExpensePurpose());
            existingExpenseManagement.setExpenseAmountSpent(updatedExpenseManagement.getExpenseAmountSpent());
            existingExpenseManagement.setExpenseTransectionType(updatedExpenseManagement.getExpenseTransectionType());
            existingExpenseManagement.setCurrencyCode(updatedExpenseManagement.getCurrencyCode());
            existingExpenseManagement.setExpenseType(updatedExpenseManagement.getExpenseType());

            // existingExpenseManagement.setWorkflowDivision(updatedExpenseManagement.getWorkflowDivision());
            // existingExpenseManagement.setWorkflowDepartment(updatedExpenseManagement.getWorkflowDepartment());
            // existingExpenseManagement.setWorkflowRole(updatedExpenseManagement.getWorkflowRole());
            // existingExpenseManagement.setRequestStatus(updatedExpenseManagement.getRequestStatus());
            // existingExpenseManagement.setWorkflowMain(updatedExpenseManagement.getWorkflowMain());
            // existingExpenseManagement.setExpenseDetails(updatedExpenseManagement.getExpenseDetails());
            // existingExpenseManagement.setEmployee(updatedExpenseManagement.getEmployee());
            // existingExpenseManagement.setCompanyRegistration(updatedExpenseManagement.getCompanyRegistration());

            return expenseManagementRepository.save(existingExpenseManagement);
        } else {
            // Handle case where entity is not found
            throw new RuntimeException("ExpenseManagement not found with id: " + id);
        }
    }

    public void deleteExpenseManagementById(Long id) {
        if (expenseManagementRepository.existsById(id)) {
            expenseManagementRepository.deleteById(id);
        } else {
            throw new RuntimeException("ExpenseManagement record not found with ID: " + id);
        }
    }












    public ExpenseManagement updateExpenseManagementWithDetails(Long id, ExpenseManagement expenseManagement,
            List<ExpenseDetails> expenseDetailsList) {
        // Update ExpenseManagement
        ExpenseManagement existingExpenseManagement = expenseManagementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExpenseManagement not found with id " + id));

        existingExpenseManagement.setExpenseFromDate(expenseManagement.getExpenseFromDate()); // Update relevant fields
        existingExpenseManagement.setExpenseTillDate(expenseManagement.getExpenseTillDate());
        existingExpenseManagement.setExpensePurpose(expenseManagement.getExpensePurpose());
        existingExpenseManagement.setExpenseAmountSpent(expenseManagement.getExpenseAmountSpent());
        existingExpenseManagement.setExpenseTransectionType(expenseManagement.getExpenseTransectionType());
        existingExpenseManagement.setExpenseType(expenseManagement.getExpenseType());
        existingExpenseManagement.setCurrencyCode(expenseManagement.getCurrencyCode());

        // Other fields to update...

        // Save the updated ExpenseManagement
        ExpenseManagement updatedExpenseManagement = expenseManagementRepository.save(existingExpenseManagement);

        // Update ExpenseDetails
        for (ExpenseDetails expenseDetails : expenseDetailsList) {
            expenseDetails.setExpenseManagement(updatedExpenseManagement); // Link to ExpenseManagement
            if (expenseDetails.getId() != null) {
                ExpenseDetails existingDetails = expenseDetailsRepository.findById(expenseDetails.getId())
                        .orElseThrow(() -> new RuntimeException("ExpenseDetails not found with id " + expenseDetails.getId()));
                existingDetails.setExpenseDate(expenseDetails.getExpenseDate());
                existingDetails.setExpenseCategory(expenseDetails.getExpenseCategory());
                existingDetails.setExpenseCost(expenseDetails.getExpenseCost());
                existingDetails.setExpenseDescription(expenseDetails.getExpenseDescription());
                existingDetails.setExpenseTransectionType(expenseDetails.getExpenseTransectionType());

    
            
                // Other fields to update...

                expenseDetailsRepository.save(existingDetails);
            } else {
                expenseDetailsRepository.save(expenseDetails); // Save new ExpenseDetails
            }
        }

        return updatedExpenseManagement;
    }
  public long getPendingExpenseCount(long companyId, long employeeId) {
        // Call the repository instance method
        return expenseManagementRepository.countByCompanyRegistrationIdAndEmployeeIdAndRequestStatus(
                companyId, employeeId, "PENDING");
    }
 
    public long countPendingRequestsByCriteria(long companyId, String workflowDivision,
            String workflowDepartment, String workflowRole) {
        // Call the repository method to get the count
        return expenseManagementRepository.countPendingRequests(companyId, workflowDivision, workflowDepartment,
                workflowRole);
    }
 
    public List<ExpenseManagement> getExpensesByDateRange(long companyId, long employeeId, Date startDate, Date endDate) {
        // Call the repository method to fetch data based on date range
        return expenseManagementRepository.findByDateRangeAndEmployeeId(companyId, employeeId, startDate, endDate);
    }



    public List<ExpenseManagement> findByCriteriaList(long companyId, List<Long> projectIds, String workflowDivision, String workflowDepartment, String workflowRole) {
        return expenseManagementRepository.findByCompanyAndProjects(
                companyId, projectIds, workflowDivision, workflowDepartment, workflowRole);
    }



    
    public List<ExpenseManagement> getApprovedExpensesByEmployeeAndDateRange(Long employeeId, Date fromDate, Date toDate) {
        return expenseManagementRepository.findByEmployeeIdAndRequestStatusAndExpenseFromDateBetween(
            employeeId, "APPROVED", fromDate, toDate
        );
    }
}
