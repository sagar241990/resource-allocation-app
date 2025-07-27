package com.sag.res_allocation_app.repository;

import com.sag.res_allocation_app.model.ProjectAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAllocationRepository extends JpaRepository<ProjectAllocation, Long> {
}
