package com.sag.res_allocation_app.resource_allocation_app.repository;

import com.sag.res_allocation_app.resource_allocation_app.model.ProjectAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ProjectAllocation entity
 */
@Repository
public interface ProjectAllocationRepository extends JpaRepository<ProjectAllocation, Long> {

    /**
     * Find by work request number
     */
    Optional<ProjectAllocation> findByWorkRequestNumber(String workRequestNumber);

    /**
     * Find all allocations by project type
     */
    List<ProjectAllocation> findByProjectType(String projectType);

    /**
     * Find all allocations by LOB (Line of Business)
     */
    List<ProjectAllocation> findByLob(String lob);

    /**
     * Find all allocations by SOE ID
     */
    List<ProjectAllocation> findBySoeId(String soeId);

    /**
     * Find all allocations by project name containing (case-insensitive)
     */
    List<ProjectAllocation> findByProjectNameContainingIgnoreCase(String projectName);

    /**
     * Check if work request number already exists
     */
    boolean existsByWorkRequestNumber(String workRequestNumber);

    /**
     * Custom query to find allocations by multiple criteria
     */
    @Query("SELECT pa FROM ProjectAllocation pa WHERE " +
           "(:projectType IS NULL OR pa.projectType = :projectType) AND " +
           "(:lob IS NULL OR pa.lob = :lob) AND " +
           "(:soeId IS NULL OR pa.soeId = :soeId)")
    List<ProjectAllocation> findByMultipleCriteria(@Param("projectType") String projectType,
                                                  @Param("lob") String lob,
                                                  @Param("soeId") String soeId);
}
