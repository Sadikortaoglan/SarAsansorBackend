package com.saraasansor.api.dto;

import com.saraasansor.api.model.Inspection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InspectionDto {
    private Long id;
    
    @NotNull(message = "Elevator ID cannot be empty")
    private Long elevatorId;
    
    private String elevatorBuildingName;
    private String elevatorIdentityNumber;
    
    @NotNull(message = "Date cannot be empty")
    private LocalDate date;
    
    @NotBlank(message = "Result cannot be empty")
    private String result;
    
    private String description;
    private LocalDateTime createdAt;

    public InspectionDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(Long elevatorId) {
        this.elevatorId = elevatorId;
    }

    public String getElevatorBuildingName() {
        return elevatorBuildingName;
    }

    public void setElevatorBuildingName(String elevatorBuildingName) {
        this.elevatorBuildingName = elevatorBuildingName;
    }

    public String getElevatorIdentityNumber() {
        return elevatorIdentityNumber;
    }

    public void setElevatorIdentityNumber(String elevatorIdentityNumber) {
        this.elevatorIdentityNumber = elevatorIdentityNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static InspectionDto fromEntity(Inspection inspection) {
        InspectionDto dto = new InspectionDto();
        dto.setId(inspection.getId());
        dto.setElevatorId(inspection.getElevator().getId());
        dto.setElevatorBuildingName(inspection.getElevator().getBuildingName());
        dto.setElevatorIdentityNumber(inspection.getElevator().getIdentityNumber());
        dto.setDate(inspection.getDate());
        dto.setResult(inspection.getResult());
        dto.setDescription(inspection.getDescription());
        dto.setCreatedAt(inspection.getCreatedAt());
        return dto;
    }
}
