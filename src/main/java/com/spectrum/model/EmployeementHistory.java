    package com.spectrum.model;



    import java.util.Date;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonIgnore;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.Table;

    @Entity
    @Table
    public class EmployeementHistory {

    @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


		@Column(length=100)
        private String companyName;

		@Column(length=50)
        private String jobRole;

		@Column(length=100)
        private String responsibilities;
        @Column
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private Date startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Column
        private Date endDate;

		@Column(length=50)
        private String jobDuration;
        @Column 
        private int latestCtc;
		@Column(length=100)
        private String supervisorContact;
		@Column(length=100)
        private String reasonOfLeaving;
		@Column(length=250)
        private String achievements;
		@Column(length=100)
        private String employeementType;
		@Column(length=100)
        private String location;
		@Column(length=100)
        private String industry;
		@Column(length=100)
        private String companySize;
		@Column(length=100)
        private String latestMonthGross;
        @Column
        private int teamSize;
        
        @ManyToOne
        @JsonIgnore
            @JoinColumn(name = "employee_id")
            private Employee employee;


            
            public EmployeementHistory() {
                super();

            }



            public EmployeementHistory(Long id, String companyName, String jobRole, String responsibilities, Date startDate,
                    Date endDate, String jobDuration, int latestCtc, String supervisorContact, String reasonOfLeaving,
                    String achievements, String employeementType, String location, String industry, String companySize,
                    String latestMonthGross, int teamSize, Employee employee) {
                this.id = id;
                this.companyName = companyName;
                this.jobRole = jobRole;
                this.responsibilities = responsibilities;
                this.startDate = startDate;
                this.endDate = endDate;
                this.jobDuration = jobDuration;
                this.latestCtc = latestCtc;
                this.supervisorContact = supervisorContact;
                this.reasonOfLeaving = reasonOfLeaving;
                this.achievements = achievements;
                this.employeementType = employeementType;
                this.location = location;
                this.industry = industry;
                this.companySize = companySize;
                this.latestMonthGross = latestMonthGross;
                this.teamSize = teamSize;
                this.employee = employee;
            }



            public Long getId() {
                return id;
            }



            public void setId(Long id) {
                this.id = id;
            }



            public String getCompanyName() {
                return companyName;
            }



            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }



            public String getJobRole() {
                return jobRole;
            }



            public void setJobRole(String jobRole) {
                this.jobRole = jobRole;
            }



            public String getResponsibilities() {
                return responsibilities;
            }



            public void setResponsibilities(String responsibilities) {
                this.responsibilities = responsibilities;
            }



            public Date getStartDate() {
                return startDate;
            }



            public void setStartDate(Date startDate) {
                this.startDate = startDate;
            }



            public Date getEndDate() {
                return endDate;
            }



            public void setEndDate(Date endDate) {
                this.endDate = endDate;
            }



            public String getJobDuration() {
                return jobDuration;
            }



            public void setJobDuration(String jobDuration) {
                this.jobDuration = jobDuration;
            }



            public int getLatestCtc() {
                return latestCtc;
            }



            public void setLatestCtc(int latestCtc) {
                this.latestCtc = latestCtc;
            }



            public String getSupervisorContact() {
                return supervisorContact;
            }



            public void setSupervisorContact(String supervisorContact) {
                this.supervisorContact = supervisorContact;
            }



            public String getReasonOfLeaving() {
                return reasonOfLeaving;
            }



            public void setReasonOfLeaving(String reasonOfLeaving) {
                this.reasonOfLeaving = reasonOfLeaving;
            }



            public String getAchievements() {
                return achievements;
            }



            public void setAchievements(String achievements) {
                this.achievements = achievements;
            }



            public String getEmployeementType() {
                return employeementType;
            }



            public void setEmployeementType(String employeementType) {
                this.employeementType = employeementType;
            }



            public String getLocation() {
                return location;
            }



            public void setLocation(String location) {
                this.location = location;
            }



            public String getIndustry() {
                return industry;
            }



            public void setIndustry(String industry) {
                this.industry = industry;
            }



            public String getCompanySize() {
                return companySize;
            }



            public void setCompanySize(String companySize) {
                this.companySize = companySize;
            }



            public String getLatestMonthGross() {
                return latestMonthGross;
            }



            public void setLatestMonthGross(String latestMonthGross) {
                this.latestMonthGross = latestMonthGross;
            }



            public int getTeamSize() {
                return teamSize;
            }



            public void setTeamSize(int teamSize) {
                this.teamSize = teamSize;
            }



            public Employee getEmployee() {
                return employee;
            }



            public void setEmployee(Employee employee) {
                this.employee = employee;
            }



            @Override
            public String toString() {
                return "EmployeementHistory [id=" + id + ", companyName=" + companyName + ", jobRole=" + jobRole
                        + ", responsibilities=" + responsibilities + ", startDate=" + startDate + ", endDate=" + endDate
                        + ", jobDuration=" + jobDuration + ", latestCtc=" + latestCtc + ", supervisorContact="
                        + supervisorContact + ", reasonOfLeaving=" + reasonOfLeaving + ", achievements=" + achievements
                        + ", employeementType=" + employeementType + ", location=" + location + ", industry=" + industry
                        + ", companySize=" + companySize + ", latestMonthGross=" + latestMonthGross + ", teamSize="
                        + teamSize + ", employee=" + employee + "]";
            }

            





    }
