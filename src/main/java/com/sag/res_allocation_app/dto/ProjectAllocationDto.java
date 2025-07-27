package com.sag.res_allocation_app.dto;

public class ProjectAllocationDto {
    private String workRequestNumber;
    private String wrName;
    private String soeId;
    private String projectName;
    private String projectType;
    private String lob;

    // getters and setters
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
}
