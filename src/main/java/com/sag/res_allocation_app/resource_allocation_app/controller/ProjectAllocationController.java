package com.sag.res_allocation_app.resource_allocation_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sag.res_allocation_app.resource_allocation_app.dto.FileUploadResponseDto;
import com.sag.res_allocation_app.resource_allocation_app.model.ProjectAllocation;
import com.sag.res_allocation_app.resource_allocation_app.service.FileUploadService;

/**
 * REST Controller for file upload and project allocation management
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // For future Angular frontend integration
public class ProjectAllocationController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * Upload CSV or Excel file with project allocation data
     */
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponseDto> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileUploadResponseDto response = fileUploadService.processUploadedFile(file);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            FileUploadResponseDto errorResponse = new FileUploadResponseDto(
                false, 
                "Internal server error: " + e.getMessage(), 
                0, 0, 0
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get all project allocations
     */
    @GetMapping("/allocations")
    public ResponseEntity<List<ProjectAllocation>> getAllAllocations() {
        try {
            List<ProjectAllocation> allocations = fileUploadService.getAllAllocations();
            return ResponseEntity.ok(allocations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get allocations by project type
     */
    @GetMapping("/allocations/project-type/{projectType}")
    public ResponseEntity<List<ProjectAllocation>> getAllocationsByProjectType(@PathVariable String projectType) {
        try {
            List<ProjectAllocation> allocations = fileUploadService.getAllocationsByProjectType(projectType);
            return ResponseEntity.ok(allocations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get allocations by LOB
     */
    @GetMapping("/allocations/lob/{lob}")
    public ResponseEntity<List<ProjectAllocation>> getAllocationsByLob(@PathVariable String lob) {
        try {
            List<ProjectAllocation> allocations = fileUploadService.getAllocationsByLob(lob);
            return ResponseEntity.ok(allocations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get allocations by SOE ID
     */
    @GetMapping("/allocations/soe-id/{soeId}")
    public ResponseEntity<List<ProjectAllocation>> getAllocationsBySoeId(@PathVariable String soeId) {
        try {
            List<ProjectAllocation> allocations = fileUploadService.getAllocationsBySoeId(soeId);
            return ResponseEntity.ok(allocations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Project Allocation App is running!");
    }
}
