package com.sag.res_allocation_app.resource_allocation_app.service;

import com.sag.res_allocation_app.resource_allocation_app.dto.ProjectAllocationDTO;
import com.sag.res_allocation_app.resource_allocation_app.model.ProjectAllocation;
import com.sag.res_allocation_app.resource_allocation_app.repository.ProjectAllocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for ProjectAllocation business logic
 */
@Service
public class ProjectAllocationService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectAllocationService.class);

    @Autowired
    private ProjectAllocationRepository projectAllocationRepository;

    /**
     * Save a new project allocation
     */
    public ProjectAllocation saveProjectAllocation(ProjectAllocationDTO dto) {
        logger.debug("Saving project allocation: {}", dto.getWorkRequestNumber());
        
        ProjectAllocation allocation = convertToEntity(dto);
        return projectAllocationRepository.save(allocation);
    }

    /**
     * Get all project allocations
     */
    public List<ProjectAllocationDTO> getAllProjectAllocations() {
        List<ProjectAllocation> allocations = projectAllocationRepository.findAll();
        return allocations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get project allocation by ID
     */
    public Optional<ProjectAllocationDTO> getProjectAllocationById(Long id) {
        Optional<ProjectAllocation> allocation = projectAllocationRepository.findById(id);
        return allocation.map(this::convertToDTO);
    }

    /**
     * Get project allocation by work request number
     */
    public Optional<ProjectAllocationDTO> getProjectAllocationByWorkRequestNumber(String workRequestNumber) {
        Optional<ProjectAllocation> allocation = projectAllocationRepository.findByWorkRequestNumber(workRequestNumber);
        return allocation.map(this::convertToDTO);
    }

    /**
     * Check if work request number exists
     */
    public boolean existsByWorkRequestNumber(String workRequestNumber) {
        return projectAllocationRepository.existsByWorkRequestNumber(workRequestNumber);
    }

    /**
     * Get project allocations by project type
     */
    public List<ProjectAllocationDTO> getProjectAllocationsByProjectType(String projectType) {
        List<ProjectAllocation> allocations = projectAllocationRepository.findByProjectType(projectType);
        return allocations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get project allocations by LOB
     */
    public List<ProjectAllocationDTO> getProjectAllocationsByLob(String lob) {
        List<ProjectAllocation> allocations = projectAllocationRepository.findByLob(lob);
        return allocations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get project allocations by SOE ID
     */
    public List<ProjectAllocationDTO> getProjectAllocationsBySoeId(String soeId) {
        List<ProjectAllocation> allocations = projectAllocationRepository.findBySoeId(soeId);
        return allocations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search project allocations by project name
     */
    public List<ProjectAllocationDTO> searchByProjectName(String projectName) {
        List<ProjectAllocation> allocations = projectAllocationRepository.findByProjectNameContainingIgnoreCase(projectName);
        return allocations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update project allocation
     */
    public Optional<ProjectAllocation> updateProjectAllocation(Long id, ProjectAllocationDTO dto) {
        Optional<ProjectAllocation> existingAllocation = projectAllocationRepository.findById(id);
        
        if (existingAllocation.isPresent()) {
            ProjectAllocation allocation = existingAllocation.get();
            updateEntityFromDTO(allocation, dto);
            return Optional.of(projectAllocationRepository.save(allocation));
        }
        
        return Optional.empty();
    }

    /**
     * Delete project allocation
     */
    public boolean deleteProjectAllocation(Long id) {
        if (projectAllocationRepository.existsById(id)) {
            projectAllocationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get total count of project allocations
     */
    public long getTotalCount() {
        return projectAllocationRepository.count();
    }

    /**
     * Convert Entity to DTO
     */
    private ProjectAllocationDTO convertToDTO(ProjectAllocation allocation) {
        ProjectAllocationDTO dto = new ProjectAllocationDTO();
        dto.setId(allocation.getId());
        dto.setWorkRequestNumber(allocation.getWorkRequestNumber());
        dto.setWrName(allocation.getWrName());
        dto.setSoeId(allocation.getSoeId());
        dto.setProjectName(allocation.getProjectName());
        dto.setProjectType(allocation.getProjectType());
        dto.setLob(allocation.getLob());
        return dto;
    }

    /**
     * Convert DTO to Entity
     */
    private ProjectAllocation convertToEntity(ProjectAllocationDTO dto) {
        ProjectAllocation allocation = new ProjectAllocation();
        allocation.setWorkRequestNumber(dto.getWorkRequestNumber());
        allocation.setWrName(dto.getWrName());
        allocation.setSoeId(dto.getSoeId());
        allocation.setProjectName(dto.getProjectName());
        allocation.setProjectType(dto.getProjectType());
        allocation.setLob(dto.getLob());
        return allocation;
    }

    /**
     * Update entity from DTO
     */
    private void updateEntityFromDTO(ProjectAllocation allocation, ProjectAllocationDTO dto) {
        allocation.setWorkRequestNumber(dto.getWorkRequestNumber());
        allocation.setWrName(dto.getWrName());
        allocation.setSoeId(dto.getSoeId());
        allocation.setProjectName(dto.getProjectName());
        allocation.setProjectType(dto.getProjectType());
        allocation.setLob(dto.getLob());
    }
}
