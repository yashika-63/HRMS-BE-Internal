package com.spectrum.workflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.service.EmailService;
import com.spectrum.workflow.model.ExpenseApproval;
import com.spectrum.workflow.model.ExpenseManagement;
import com.spectrum.workflow.repository.ExpenseApprovalRepository;
import com.spectrum.workflow.repository.ExpenseManagementRepository;

@Service
public class ExpenseApprovalService {

    @Autowired
    private ExpenseApprovalRepository expenseApprovalRepository;

    @Autowired
    private ExpenseManagementRepository expenseManagementRepository;

    @Autowired
    private EmailService emailService;

    public ExpenseApproval saveAndNotify(Long expenseManagementId, ExpenseApproval expenseApproval) throws Exception {
        // Retrieve the ExpenseManagement using the expenseManagementId
        ExpenseManagement expenseManagement = expenseManagementRepository.findById(expenseManagementId)
                .orElseThrow(() -> new Exception("Expense Application not found with id: " + expenseManagementId));

        // Retrieve the associated Employee and CompanyRegistration
        Employee employee = expenseManagement.getEmployee();
        CompanyRegistration company = expenseManagement.getCompanyRegistration();

        // Set the ExpenseManagement for the ExpenseApproval
        expenseApproval.setExpenseManagement(expenseManagement);

        // Save the ExpenseApproval object
        ExpenseApproval savedExpenseApproval = expenseApprovalRepository.save(expenseApproval);

        // Send email notification
        String toEmail = expenseApproval.getMail();
        String subject = "Expense Approval Notification";
        String message = String.format(
                "Dear %s %s %s,\n\nYour expense request (ID: %d) has been %s.\n" +
                        "Note: %s\n\nCompany: %s\nEmployee: %s\nDesignation: %s\n\nBest regards,\n%s",
                employee != null ? employee.getFirstName() : "N/A",
                employee != null ? employee.getMiddleName() : "N/A",
                employee != null ? employee.getLastName() : "N/A",
                expenseManagementId,
                expenseApproval.isAction() ? "approved" : "rejected",
                expenseApproval.getNote(),
                company != null ? company.getCompanyName() : "N/A",
                employee != null ? employee.getFirstName() : "N/A",
                employee != null ? employee.getDesignation() : "N/A",
                company != null ? company.getCompanyName() : "N/A");

        emailService.sendEmail(toEmail, subject, message);

        return savedExpenseApproval;
    }


    public List<ExpenseApproval> getAllByExpenseManagementId(Long expenseManagementId) {
        return expenseApprovalRepository.findByExpenseManagementId(expenseManagementId);
    }
}
