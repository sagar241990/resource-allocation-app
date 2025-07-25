package com.sag.res_allocation_app.resource_allocation_app.dto;

/**
 * Response DTO for file upload operations
 */
public class FileUploadResponse {

    private boolean success;
    private String message;
    private int recordsProcessed;
    private int recordsSkipped;
    private int totalRecords;

    // Default constructor
    public FileUploadResponse() {
    }

    // Constructor
    public FileUploadResponse(boolean success, String message, int recordsProcessed, 
                             int recordsSkipped, int totalRecords) {
        this.success = success;
        this.message = message;
        this.recordsProcessed = recordsProcessed;
        this.recordsSkipped = recordsSkipped;
        this.totalRecords = totalRecords;
    }

    // Static factory methods
    public static FileUploadResponse success(String message, int recordsProcessed, 
                                           int recordsSkipped, int totalRecords) {
        return new FileUploadResponse(true, message, recordsProcessed, recordsSkipped, totalRecords);
    }

    public static FileUploadResponse failure(String message) {
        return new FileUploadResponse(false, message, 0, 0, 0);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(int recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public int getRecordsSkipped() {
        return recordsSkipped;
    }

    public void setRecordsSkipped(int recordsSkipped) {
        this.recordsSkipped = recordsSkipped;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Override
    public String toString() {
        return "FileUploadResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", recordsProcessed=" + recordsProcessed +
                ", recordsSkipped=" + recordsSkipped +
                ", totalRecords=" + totalRecords +
                '}';
    }
}
