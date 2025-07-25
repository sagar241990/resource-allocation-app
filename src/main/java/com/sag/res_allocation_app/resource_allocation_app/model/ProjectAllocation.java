package com.sag.res_allocation_app.resource_allocation_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing Project Allocation data
 */
@Entity
@Table(name = "project_allocation")
public class ProjectAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_request_number", nullable = false)
    private String workRequestNumber;

    @Column(name = "wr_name", nullable = false)
    private String wrName;

    @Column(name = "soe_id", nullable = false)
    private String soeId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_type")
    private String projectType;

    @Column(name = "lob")
    private String lob;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public ProjectAllocation() {
    }

    // Constructor
    public ProjectAllocation(String workRequestNumber, String wrName, String soeId, 
                           String projectName, String projectType, String lob) {
        this.workRequestNumber = workRequestNumber;
        this.wrName = wrName;
        this.soeId = soeId;
        this.projectName = projectName;
        this.projectType = projectType;
        this.lob = lob;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ProjectAllocation{" +
                "id=" + id +
                ", workRequestNumber='" + workRequestNumber + '\'' +
                ", wrName='" + wrName + '\'' +
                ", soeId='" + soeId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectType='" + projectType + '\'' +
                ", lob='" + lob + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
