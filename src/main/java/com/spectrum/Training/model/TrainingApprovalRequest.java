package com.spectrum.Training.model;
public class TrainingApprovalRequest {
    private Long id;
    private Boolean managerApproval;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getManagerApproval() {
        return managerApproval;
    }

    public void setManagerApproval(Boolean managerApproval) {
        this.managerApproval = managerApproval;
    }
}
