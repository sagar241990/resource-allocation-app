package com.sag.res_allocation_app.resource_allocation_app.util;

import com.sag.res_allocation_app.resource_allocation_app.dto.ProjectAllocationDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for parsing CSV and Excel files
 */
@Component
public class FileParserUtil {

    private static final String[] EXPECTED_HEADERS = {
        "workRequestNumber", "wrName", "soeId", "projectName", "projectType", "lob"
    };

    /**
     * Parse CSV file and return list of ProjectAllocationDto
     */
    public List<ProjectAllocationDto> parseCsvFile(MultipartFile file) throws IOException {
        List<ProjectAllocationDto> allocations = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    validateCsvHeaders(line);
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    ProjectAllocationDto dto = parseCsvLine(line, lineNumber);
                    if (dto != null) {
                        allocations.add(dto);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error parsing line " + lineNumber + ": " + e.getMessage(), e);
                }
            }
        }
        
        return allocations;
    }

    /**
     * Parse Excel file and return list of ProjectAllocationDto
     */
    public List<ProjectAllocationDto> parseExcelFile(MultipartFile file) throws IOException {
        List<ProjectAllocationDto> allocations = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Get first sheet
            
            boolean isFirstRow = true;
            
            for (Row row : sheet) {
                // Skip header row
                if (isFirstRow) {
                    isFirstRow = false;
                    validateExcelHeaders(row);
                    continue;
                }
                
                // Skip empty rows
                if (isRowEmpty(row)) {
                    continue;
                }
                
                try {
                    ProjectAllocationDto dto = parseExcelRow(row);
                    if (dto != null) {
                        allocations.add(dto);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error parsing row " + (row.getRowNum() + 1) + ": " + e.getMessage(), e);
                }
            }
        }
        
        return allocations;
    }

    /**
     * Validate CSV headers
     */
    private void validateCsvHeaders(String headerLine) {
        String[] headers = headerLine.split(",");
        if (headers.length != EXPECTED_HEADERS.length) {
            throw new RuntimeException("Invalid number of columns. Expected " + EXPECTED_HEADERS.length + " but found " + headers.length);
        }
        
        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            String header = headers[i].trim().replaceAll("\"", "");
            if (!header.equalsIgnoreCase(EXPECTED_HEADERS[i])) {
                throw new RuntimeException("Invalid header at column " + (i + 1) + ". Expected '" + EXPECTED_HEADERS[i] + "' but found '" + header + "'");
            }
        }
    }

    /**
     * Validate Excel headers
     */
    private void validateExcelHeaders(Row headerRow) {
        if (headerRow.getLastCellNum() != EXPECTED_HEADERS.length) {
            throw new RuntimeException("Invalid number of columns. Expected " + EXPECTED_HEADERS.length + " but found " + headerRow.getLastCellNum());
        }
        
        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            Cell cell = headerRow.getCell(i);
            String header = getCellValueAsString(cell);
            if (!header.equalsIgnoreCase(EXPECTED_HEADERS[i])) {
                throw new RuntimeException("Invalid header at column " + (i + 1) + ". Expected '" + EXPECTED_HEADERS[i] + "' but found '" + header + "'");
            }
        }
    }

    /**
     * Parse a single CSV line
     */
    private ProjectAllocationDto parseCsvLine(String line, int lineNumber) {
        String[] values = line.split(",");
        
        if (values.length != EXPECTED_HEADERS.length) {
            throw new RuntimeException("Invalid number of columns at line " + lineNumber + ". Expected " + EXPECTED_HEADERS.length + " but found " + values.length);
        }
        
        // Clean and validate values
        String workRequestNumber = cleanValue(values[0]);
        String wrName = cleanValue(values[1]);
        String soeId = cleanValue(values[2]);
        String projectName = cleanValue(values[3]);
        String projectType = cleanValue(values[4]);
        String lob = cleanValue(values[5]);
        
        validateRequiredFields(workRequestNumber, wrName, soeId, projectName, projectType, lob, lineNumber);
        
        return new ProjectAllocationDto(workRequestNumber, wrName, soeId, projectName, projectType, lob);
    }

    /**
     * Parse a single Excel row
     */
    private ProjectAllocationDto parseExcelRow(Row row) {
        String workRequestNumber = getCellValueAsString(row.getCell(0));
        String wrName = getCellValueAsString(row.getCell(1));
        String soeId = getCellValueAsString(row.getCell(2));
        String projectName = getCellValueAsString(row.getCell(3));
        String projectType = getCellValueAsString(row.getCell(4));
        String lob = getCellValueAsString(row.getCell(5));
        
        validateRequiredFields(workRequestNumber, wrName, soeId, projectName, projectType, lob, row.getRowNum() + 1);
        
        return new ProjectAllocationDto(workRequestNumber, wrName, soeId, projectName, projectType, lob);
    }

    /**
     * Get cell value as string
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue()).trim();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Check if a row is empty
     */
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        
        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !getCellValueAsString(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clean CSV value (remove quotes and trim)
     */
    private String cleanValue(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replaceAll("^\"|\"$", "");
    }

    /**
     * Validate required fields
     */
    private void validateRequiredFields(String workRequestNumber, String wrName, String soeId,
                                      String projectName, String projectType, String lob, int lineNumber) {
        List<String> missingFields = new ArrayList<>();
        
        if (workRequestNumber == null || workRequestNumber.isEmpty()) {
            missingFields.add("workRequestNumber");
        }
        if (wrName == null || wrName.isEmpty()) {
            missingFields.add("wrName");
        }
        if (soeId == null || soeId.isEmpty()) {
            missingFields.add("soeId");
        }
        if (projectName == null || projectName.isEmpty()) {
            missingFields.add("projectName");
        }
        if (projectType == null || projectType.isEmpty()) {
            missingFields.add("projectType");
        }
        if (lob == null || lob.isEmpty()) {
            missingFields.add("lob");
        }
        
        if (!missingFields.isEmpty()) {
            throw new RuntimeException("Missing required fields at line " + lineNumber + ": " + String.join(", ", missingFields));
        }
    }
}
