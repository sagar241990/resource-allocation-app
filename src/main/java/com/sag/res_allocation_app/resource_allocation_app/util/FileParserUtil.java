package com.sag.res_allocation_app.resource_allocation_app.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sag.res_allocation_app.resource_allocation_app.dto.ProjectAllocationDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for parsing CSV and Excel files
 */
@Component
public class FileParserUtil {

    // Expected column headers
    private static final String[] EXPECTED_HEADERS = {
        "workRequestNumber", "wrName", "soeId", "projectName", "projectType", "lob"
    };

    /**
     * Parse CSV file and convert to ProjectAllocationDTO list
     */
    public List<ProjectAllocationDTO> parseCSV(MultipartFile file) throws IOException, CsvException {
        List<ProjectAllocationDTO> allocations = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty");
            }

            // Skip header row if present
            String[] headers = rows.get(0);
            validateHeaders(headers);

            // Process data rows
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length >= 6) {
                    ProjectAllocationDTO dto = createProjectAllocationDTO(row);
                    if (isValidRecord(dto)) {
                        allocations.add(dto);
                    }
                }
            }
        }

        return allocations;
    }

    /**
     * Parse Excel file and convert to ProjectAllocationDTO list
     */
    public List<ProjectAllocationDTO> parseExcel(MultipartFile file) throws IOException {
        List<ProjectAllocationDTO> allocations = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new IllegalArgumentException("Excel file is empty");
            }

            // Validate headers
            Row headerRow = sheet.getRow(0);
            if (headerRow != null) {
                validateExcelHeaders(headerRow);
            }

            // Process data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    ProjectAllocationDTO dto = createProjectAllocationDTOFromExcel(row);
                    if (isValidRecord(dto)) {
                        allocations.add(dto);
                    }
                }
            }
        }

        return allocations;
    }

    /**
     * Validate CSV headers
     */
    private void validateHeaders(String[] headers) {
        if (headers.length < EXPECTED_HEADERS.length) {
            throw new IllegalArgumentException("CSV file must contain at least " + 
                EXPECTED_HEADERS.length + " columns");
        }
    }

    /**
     * Validate Excel headers
     */
    private void validateExcelHeaders(Row headerRow) {
        if (headerRow.getPhysicalNumberOfCells() < EXPECTED_HEADERS.length) {
            throw new IllegalArgumentException("Excel file must contain at least " + 
                EXPECTED_HEADERS.length + " columns");
        }
    }

    /**
     * Create ProjectAllocationDTO from CSV row
     */
    private ProjectAllocationDTO createProjectAllocationDTO(String[] row) {
        ProjectAllocationDTO dto = new ProjectAllocationDTO();
        dto.setWorkRequestNumber(getCellValue(row, 0));
        dto.setWrName(getCellValue(row, 1));
        dto.setSoeId(getCellValue(row, 2));
        dto.setProjectName(getCellValue(row, 3));
        dto.setProjectType(getCellValue(row, 4));
        dto.setLob(getCellValue(row, 5));
        return dto;
    }

    /**
     * Create ProjectAllocationDTO from Excel row
     */
    private ProjectAllocationDTO createProjectAllocationDTOFromExcel(Row row) {
        ProjectAllocationDTO dto = new ProjectAllocationDTO();
        dto.setWorkRequestNumber(getExcelCellValue(row, 0));
        dto.setWrName(getExcelCellValue(row, 1));
        dto.setSoeId(getExcelCellValue(row, 2));
        dto.setProjectName(getExcelCellValue(row, 3));
        dto.setProjectType(getExcelCellValue(row, 4));
        dto.setLob(getExcelCellValue(row, 5));
        return dto;
    }

    /**
     * Get cell value from CSV row with null check
     */
    private String getCellValue(String[] row, int index) {
        if (index < row.length && row[index] != null) {
            return row[index].trim();
        }
        return "";
    }

    /**
     * Get cell value from Excel row with type handling
     */
    private String getExcelCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
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
                    return String.valueOf((long) cell.getNumericCellValue());
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
     * Validate if the record has required fields
     */
    private boolean isValidRecord(ProjectAllocationDTO dto) {
        return dto.getWorkRequestNumber() != null && !dto.getWorkRequestNumber().isEmpty() &&
               dto.getWrName() != null && !dto.getWrName().isEmpty() &&
               dto.getSoeId() != null && !dto.getSoeId().isEmpty() &&
               dto.getProjectName() != null && !dto.getProjectName().isEmpty();
    }

    /**
     * Check if file is CSV format
     */
    public boolean isCSVFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && filename.toLowerCase().endsWith(".csv");
    }

    /**
     * Check if file is Excel format
     */
    public boolean isExcelFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && (filename.toLowerCase().endsWith(".xlsx") || 
                                  filename.toLowerCase().endsWith(".xls"));
    }
}
