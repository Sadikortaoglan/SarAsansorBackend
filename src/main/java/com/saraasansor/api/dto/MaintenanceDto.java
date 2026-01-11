package com.saraasansor.api.dto;

import com.saraasansor.api.model.Maintenance;

import java.time.LocalDate;

public class MaintenanceDto {
    private Long id;
    private Long elevatorId;
    private String elevatorBuildingName;
    private LocalDate date;
    private String description;
    private Long technicianUserId;
    private String technicianUsername;
    private Double amount;
    private Boolean isPaid;
    private LocalDate paymentDate;
    
    public MaintenanceDto() {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTechnicianUserId() {
        return technicianUserId;
    }

    public void setTechnicianUserId(Long technicianUserId) {
        this.technicianUserId = technicianUserId;
    }

    public String getTechnicianUsername() {
        return technicianUsername;
    }

    public void setTechnicianUsername(String technicianUsername) {
        this.technicianUsername = technicianUsername;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public static MaintenanceDto fromEntity(Maintenance maintenance) {
        MaintenanceDto dto = new MaintenanceDto();
        dto.setId(maintenance.getId());
        dto.setElevatorId(maintenance.getElevator().getId());
        dto.setElevatorBuildingName(maintenance.getElevator().getBuildingName());
        dto.setDate(maintenance.getDate());
        dto.setDescription(maintenance.getDescription());
        if (maintenance.getTechnician() != null) {
            dto.setTechnicianUserId(maintenance.getTechnician().getId());
            dto.setTechnicianUsername(maintenance.getTechnician().getUsername());
        }
        dto.setAmount(maintenance.getAmount());
        dto.setIsPaid(maintenance.getIsPaid());
        dto.setPaymentDate(maintenance.getPaymentDate());
        return dto;
    }
}
