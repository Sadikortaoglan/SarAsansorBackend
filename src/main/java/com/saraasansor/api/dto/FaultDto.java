package com.saraasansor.api.dto;

import com.saraasansor.api.model.Fault;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class FaultDto {
    private Long id;
    
    @NotNull(message = "Elevator ID cannot be empty")
    private Long elevatorId;
    
    private String elevatorBuildingName;
    private String elevatorIdentityNumber;
    
    @NotBlank(message = "Fault subject cannot be empty")
    private String faultSubject;
    
    @NotBlank(message = "Contact person cannot be empty")
    private String contactPerson;
    
    private String buildingAuthorizedMessage;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    public FaultDto() {
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

    public String getFaultSubject() {
        return faultSubject;
    }

    public void setFaultSubject(String faultSubject) {
        this.faultSubject = faultSubject;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getBuildingAuthorizedMessage() {
        return buildingAuthorizedMessage;
    }

    public void setBuildingAuthorizedMessage(String buildingAuthorizedMessage) {
        this.buildingAuthorizedMessage = buildingAuthorizedMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static FaultDto fromEntity(Fault fault) {
        FaultDto dto = new FaultDto();
        dto.setId(fault.getId());
        dto.setElevatorId(fault.getElevator().getId());
        dto.setElevatorBuildingName(fault.getElevator().getBuildingName());
        dto.setElevatorIdentityNumber(fault.getElevator().getIdentityNumber());
        dto.setFaultSubject(fault.getFaultSubject());
        dto.setContactPerson(fault.getContactPerson());
        dto.setBuildingAuthorizedMessage(fault.getBuildingAuthorizedMessage());
        dto.setDescription(fault.getDescription());
        dto.setStatus(fault.getStatus().name());
        dto.setCreatedAt(fault.getCreatedAt());
        return dto;
    }
}
