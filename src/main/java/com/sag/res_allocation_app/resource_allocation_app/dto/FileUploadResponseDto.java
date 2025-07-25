package com.sag.res_allocation_app.resource_allocation_app.dto;

import java.util.List;

/**
 * DTO for file upload response
 */
public class FileUploadResponseDto {

    private boolean success;
    private String message;
    private int totalRecords;
    private int successfulRecords;
    private int failedRecords;
    private List<String> errors;

    // Default constructor
    public FileUploadResponseDto() {}

    // Constructor for success response
    public FileUploadResponseDto(boolean success, String message, int totalRecords, 
                               int successfulRecords, int failedRecords) {
        this.success = success;
        this.message = message;
        this.totalRecords = totalRecords;
        this.successfulRecords = successfulRecords;
        this.failedRecords = failedRecords;
    }

    // Constructor with errors
    public FileUploadResponseDto(boolean success, String message, int totalRecords, 
                               int successfulRecords, int failedRecords, List<String> errors) {
        this(success, message, totalRecords, successfulRecords, failedRecords);
        this.errors = errors;
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

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getSuccessfulRecords() {
        return successfulRecords;
    }

    public void setSuccessfulRecords(int successfulRecords) {
        this.successfulRecords = successfulRecords;
    }

    public int getFailedRecords() {
        return failedRecords;
    }

    public void setFailedRecords(int failedRecords) {
        this.failedRecords = failedRecords;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "FileUploadResponseDto{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", totalRecords=" + totalRecords +
                ", successfulRecords=" + successfulRecords +
                ", failedRecords=" + failedRecords +
                ", errors=" + errors +
                '}';
    }
}
