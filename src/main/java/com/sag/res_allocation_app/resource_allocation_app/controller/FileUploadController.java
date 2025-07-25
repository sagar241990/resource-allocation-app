package com.sag.res_allocation_app.resource_allocation_app.controller;

import com.sag.res_allocation_app.resource_allocation_app.dto.FileUploadResponse;
import com.sag.res_allocation_app.resource_allocation_app.dto.ProjectAllocationDTO;
import com.sag.res_allocation_app.resource_allocation_app.service.FileUploadService;
import com.sag.res_allocation_app.resource_allocation_app.service.ProjectAllocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST Controller for file upload operations
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow frontend integration
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ProjectAllocationService projectAllocationService;

    /**
     * Upload CSV or Excel file with project allocation data
     */
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("Received file upload request: {}", file.getOriginalFilename());

        try {
            // Validate file
            String validationError = fileUploadService.validateFile(file);
            if (validationError != null) {
                return ResponseEntity.badRequest()
                        .body(FileUploadResponse.failure(validationError));
            }

            // Process file
            FileUploadResponse response = fileUploadService.processFile(file);
            
            if (response.isSuccess()) {
                logger.info("File uploaded successfully: {}", file.getOriginalFilename());
                return ResponseEntity.ok(response);
            } else {
                logger.warn("File upload failed: {}", response.getMessage());
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            logger.error("Unexpected error during file upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FileUploadResponse.failure("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Get all project allocations
     */
    @GetMapping("/allocations")
    public ResponseEntity<List<ProjectAllocationDTO>> getAllAllocations() {
        try {
            List<ProjectAllocationDTO> allocations = projectAllocationService.getAllProjectAllocations();
            return ResponseEntity.ok(allocations);
        } catch (Exception e) {
            logger.error("Error retrieving allocations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get project allocation by ID
     */
    @GetMapping("/allocations/{id}")
    public ResponseEntity<ProjectAllocationDTO> getAllocationById(@PathVariable Long id) {
        try {
            return projectAllocationService.getProjectAllocationById(id)
                    .map(allocation -> ResponseEntity.ok(allocation))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error retrieving allocation by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search allocations by work request number
     */
    @GetMapping("/allocations/search")
    public ResponseEntity<ProjectAllocationDTO> searchByWorkRequestNumber(@RequestParam String workRequestNumber) {
        try {
            return projectAllocationService.getProjectAllocationByWorkRequestNumber(workRequestNumber)
                    .map(allocation -> ResponseEntity.ok(allocation))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error searching allocation by work request number: {}", workRequestNumber, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get allocations by project type
     */
    @GetMapping("/allocations/project-type/{projectType}")
    public ResponseEntity<List<ProjectAllocationDTO>> getAllocationsByProjectType(@PathVariable String projectType) {
        try {
            List<ProjectAllocationDTO> allocations = projectAllocationService.getProjectAllocationsByProjectType(projectType);
            return ResponseEntity.ok(allocations);
        } catch (Exception e) {
            logger.error("Error retrieving allocations by project type: {}", projectType, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get allocations by LOB
     */
    @GetMapping("/allocations/lob/{lob}")
    public ResponseEntity<List<ProjectAllocationDTO>> getAllocationsByLob(@PathVariable String lob) {
        try {
            List<ProjectAllocationDTO> allocations = projectAllocationService.getProjectAllocationsByLob(lob);
            return ResponseEntity.ok(allocations);
        } catch (Exception e) {
            logger.error("Error retrieving allocations by LOB: {}", lob, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get total count of allocations
     */
    @GetMapping("/allocations/count")
    public ResponseEntity<Long> getTotalCount() {
        try {
            long count = projectAllocationService.getTotalCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error retrieving total count", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is running");
    }
}
