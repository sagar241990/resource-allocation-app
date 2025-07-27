package com.sag.res_allocation_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_allocation")
public class ProjectAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String workRequestNumber;

    private String wrName;

    @Column(nullable = false)
    private String soeId;

    private String projectName;

    private String projectType;

    private String lob;

    // getters and setters
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
}
