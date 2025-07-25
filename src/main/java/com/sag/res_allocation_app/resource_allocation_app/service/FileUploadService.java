package com.sag.res_allocation_app.resource_allocation_app.service;

import com.sag.res_allocation_app.resource_allocation_app.dto.FileUploadResponseDto;
import com.sag.res_allocation_app.resource_allocation_app.dto.ProjectAllocationDto;
import com.sag.res_allocation_app.resource_allocation_app.model.ProjectAllocation;
import com.sag.res_allocation_app.resource_allocation_app.repository.ProjectAllocationRepository;
import com.sag.res_allocation_app.resource_allocation_app.util.FileParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling file upload and processing
 */
@Service
public class FileUploadService {

    @Autowired
    private FileParserUtil fileParserUtil;

    @Autowired
    private ProjectAllocationRepository projectAllocationRepository;

    /**
     * Process uploaded file (CSV or Excel)
     */
    public FileUploadResponseDto processUploadedFile(MultipartFile file) {
        if (file.isEmpty()) {
            return new FileUploadResponseDto(false, "File is empty", 0, 0, 0);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return new FileUploadResponseDto(false, "Invalid file name", 0, 0, 0);
        }

        try {
            List<ProjectAllocationDto> allocations;
            
            // Parse file based on extension
            if (fileName.toLowerCase().endsWith(".csv")) {
                allocations = fileParserUtil.parseCsvFile(file);
            } else if (fileName.toLowerCase().endsWith(".xlsx") || fileName.toLowerCase().endsWith(".xls")) {
                allocations = fileParserUtil.parseExcelFile(file);
            } else {
                return new FileUploadResponseDto(false, "Unsupported file format. Only CSV and Excel files are supported.", 0, 0, 0);
            }

            // Process and save allocations
            return saveAllocations(allocations);

        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return new FileUploadResponseDto(false, "Error processing file: " + e.getMessage(), 0, 0, 0, errors);
        }
    }

    /**
     * Save allocations to database
     */
    private FileUploadResponseDto saveAllocations(List<ProjectAllocationDto> allocations) {
        int totalRecords = allocations.size();
        int successfulRecords = 0;
        int failedRecords = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < allocations.size(); i++) {
            try {
                ProjectAllocationDto dto = allocations.get(i);
                
                // Check if work request number already exists
                if (projectAllocationRepository.existsByWorkRequestNumber(dto.getWorkRequestNumber())) {
                    errors.add("Row " + (i + 2) + ": Work Request Number '" + dto.getWorkRequestNumber() + "' already exists");
                    failedRecords++;
                    continue;
                }

                // Convert DTO to Entity and save
                ProjectAllocation entity = convertDtoToEntity(dto);
                projectAllocationRepository.save(entity);
                successfulRecords++;

            } catch (Exception e) {
                errors.add("Row " + (i + 2) + ": " + e.getMessage());
                failedRecords++;
            }
        }

        boolean success = failedRecords == 0;
        String message = success ? 
            "File processed successfully. " + successfulRecords + " records saved." :
            "File processed with errors. " + successfulRecords + " records saved, " + failedRecords + " failed.";

        return new FileUploadResponseDto(success, message, totalRecords, successfulRecords, failedRecords, errors);
    }

    /**
     * Convert DTO to Entity
     */
    private ProjectAllocation convertDtoToEntity(ProjectAllocationDto dto) {
        return new ProjectAllocation(
            dto.getWorkRequestNumber(),
            dto.getWrName(),
            dto.getSoeId(),
            dto.getProjectName(),
            dto.getProjectType(),
            dto.getLob()
        );
    }

    /**
     * Get all project allocations
     */
    public List<ProjectAllocation> getAllAllocations() {
        return projectAllocationRepository.findAll();
    }

    /**
     * Get allocations by project type
     */
    public List<ProjectAllocation> getAllocationsByProjectType(String projectType) {
        return projectAllocationRepository.findByProjectType(projectType);
    }

    /**
     * Get allocations by LOB
     */
    public List<ProjectAllocation> getAllocationsByLob(String lob) {
        return projectAllocationRepository.findByLob(lob);
    }

    /**
     * Get allocations by SOE ID
     */
    public List<ProjectAllocation> getAllocationsBySoeId(String soeId) {
        return projectAllocationRepository.findBySoeId(soeId);
    }
}
