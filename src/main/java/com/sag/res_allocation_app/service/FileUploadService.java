package com.sag.res_allocation_app.service;

import com.sag.res_allocation_app.model.ProjectAllocation;
import com.sag.res_allocation_app.repository.ProjectAllocationRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileUploadService {

    private final ProjectAllocationRepository repository;

    public FileUploadService(ProjectAllocationRepository repository) {
        this.repository = repository;
    }

    public void uploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("File must have a name");
        }
        List<ProjectAllocation> allocations;
        if (filename.endsWith(".csv")) {
            allocations = parseCsv(file);
        } else if (filename.endsWith(".xlsx")) {
            allocations = parseExcel(file);
        } else {
            throw new IllegalArgumentException("Unsupported file type");
        }
        repository.saveAll(allocations);
    }

    private List<ProjectAllocation> parseCsv(MultipartFile file) throws IOException {
        List<ProjectAllocation> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 6) {
                    continue; // skip invalid rows
                }
                ProjectAllocation pa = new ProjectAllocation();
                pa.setWorkRequestNumber(values[0]);
                pa.setWrName(values[1]);
                pa.setSoeId(values[2]);
                pa.setProjectName(values[3]);
                pa.setProjectType(values[4]);
                pa.setLob(values[5]);
                list.add(pa);
            }
        }
        return list;
    }

    private List<ProjectAllocation> parseExcel(MultipartFile file) throws IOException {
        List<ProjectAllocation> list = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // skip header
                }
                ProjectAllocation pa = new ProjectAllocation();
                pa.setWorkRequestNumber(row.getCell(0).getStringCellValue());
                pa.setWrName(row.getCell(1).getStringCellValue());
                pa.setSoeId(row.getCell(2).getStringCellValue());
                pa.setProjectName(row.getCell(3).getStringCellValue());
                pa.setProjectType(row.getCell(4).getStringCellValue());
                pa.setLob(row.getCell(5).getStringCellValue());
                list.add(pa);
            }
        }
        return list;
    }
}
