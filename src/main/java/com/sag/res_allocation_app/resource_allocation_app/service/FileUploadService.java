package com.sag.res_allocation_app.resource_allocation_app.service;

import com.sag.res_allocation_app.resource_allocation_app.dto.FileUploadResponse;
import com.sag.res_allocation_app.resource_allocation_app.dto.ProjectAllocationDTO;
import com.sag.res_allocation_app.resource_allocation_app.util.FileParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service class for handling file upload operations
 */
@Service
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired
    private FileParserUtil fileParserUtil;

    @Autowired
    private ProjectAllocationService projectAllocationService;

    /**
     * Process uploaded file (CSV or Excel)
     */
    public FileUploadResponse processFile(MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return FileUploadResponse.failure("File is empty");
            }

            List<ProjectAllocationDTO> allocations;
            
            // Parse file based on type
            if (fileParserUtil.isCSVFile(file)) {
                logger.info("Processing CSV file: {}", file.getOriginalFilename());
                allocations = fileParserUtil.parseCSV(file);
            } else if (fileParserUtil.isExcelFile(file)) {
                logger.info("Processing Excel file: {}", file.getOriginalFilename());
                allocations = fileParserUtil.parseExcel(file);
            } else {
                return FileUploadResponse.failure("Unsupported file format. Please upload CSV or Excel files only.");
            }

            if (allocations.isEmpty()) {
                return FileUploadResponse.failure("No valid records found in the file");
            }

            // Save allocations to database
            int recordsProcessed = 0;
            int recordsSkipped = 0;

            for (ProjectAllocationDTO dto : allocations) {
                try {
                    // Check if work request number already exists
                    if (projectAllocationService.existsByWorkRequestNumber(dto.getWorkRequestNumber())) {
                        logger.warn("Skipping duplicate work request number: {}", dto.getWorkRequestNumber());
                        recordsSkipped++;
                        continue;
                    }

                    projectAllocationService.saveProjectAllocation(dto);
                    recordsProcessed++;
                } catch (Exception e) {
                    logger.error("Error processing record: {}", dto.getWorkRequestNumber(), e);
                    recordsSkipped++;
                }
            }

            String message = String.format("File processed successfully. Records processed: %d, Skipped: %d", 
                                         recordsProcessed, recordsSkipped);
            
            return FileUploadResponse.success(message, recordsProcessed, recordsSkipped, allocations.size());

        } catch (Exception e) {
            logger.error("Error processing file: {}", file.getOriginalFilename(), e);
            return FileUploadResponse.failure("Error processing file: " + e.getMessage());
        }
    }

    /**
     * Validate file format and size
     */
    public String validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        if (!fileParserUtil.isCSVFile(file) && !fileParserUtil.isExcelFile(file)) {
            return "Unsupported file format. Please upload CSV or Excel files only.";
        }

        // Check file size (10MB limit)
        if (file.getSize() > 10 * 1024 * 1024) {
            return "File size exceeds 10MB limit";
        }

        return null; // No validation errors
    }
}
