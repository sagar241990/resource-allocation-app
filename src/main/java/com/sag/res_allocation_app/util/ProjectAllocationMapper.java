package com.sag.res_allocation_app.util;

import com.sag.res_allocation_app.dto.ProjectAllocationDto;
import com.sag.res_allocation_app.model.ProjectAllocation;

public final class ProjectAllocationMapper {
    private ProjectAllocationMapper() {}

    public static ProjectAllocationDto toDto(ProjectAllocation entity) {
        ProjectAllocationDto dto = new ProjectAllocationDto();
        dto.setWorkRequestNumber(entity.getWorkRequestNumber());
        dto.setWrName(entity.getWrName());
        dto.setSoeId(entity.getSoeId());
        dto.setProjectName(entity.getProjectName());
        dto.setProjectType(entity.getProjectType());
        dto.setLob(entity.getLob());
        return dto;
    }

    public static ProjectAllocation toEntity(ProjectAllocationDto dto) {
        ProjectAllocation entity = new ProjectAllocation();
        entity.setWorkRequestNumber(dto.getWorkRequestNumber());
        entity.setWrName(dto.getWrName());
        entity.setSoeId(dto.getSoeId());
        entity.setProjectName(dto.getProjectName());
        entity.setProjectType(dto.getProjectType());
        entity.setLob(dto.getLob());
        return entity;
    }
}
