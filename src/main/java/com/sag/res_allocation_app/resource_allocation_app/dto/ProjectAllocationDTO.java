package com.sag.res_allocation_app.resource_allocation_app.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for Project Allocation data transfer
 */
public class ProjectAllocationDTO {

    private Long id;

    @NotBlank(message = "Work Request Number is required")
    private String workRequestNumber;

    @NotBlank(message = "WR Name is required")
    private String wrName;

    @NotBlank(message = "SOE ID is required")
    private String soeId;

    @NotBlank(message = "Project Name is required")
    private String projectName;

    private String projectType;

    private String lob;

    // Default constructor
    public ProjectAllocationDTO() {
    }

    // Constructor
    public ProjectAllocationDTO(String workRequestNumber, String wrName, String soeId, 
                               String projectName, String projectType, String lob) {
        this.workRequestNumber = workRequestNumber;
        this.wrName = wrName;
        this.soeId = soeId;
        this.projectName = projectName;
        this.projectType = projectType;
        this.lob = lob;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkRequestNumber() {
        return workRequestNumber;
    }

    public void setWorkRequestNumber(String workRequestNumber) {
        this.workRequestNumber = workRequestNumber;
    }

    public String getWrName() {
        return wrName;
    }

    public void setWrName(String wrName) {
        this.wrName = wrName;
    }

    public String getSoeId() {
        return soeId;
    }

    public void setSoeId(String soeId) {
        this.soeId = soeId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    @Override
    public String toString() {
        return "ProjectAllocationDTO{" +
                "id=" + id +
                ", workRequestNumber='" + workRequestNumber + '\'' +
                ", wrName='" + wrName + '\'' +
                ", soeId='" + soeId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectType='" + projectType + '\'' +
                ", lob='" + lob + '\'' +
                '}';
    }
}
