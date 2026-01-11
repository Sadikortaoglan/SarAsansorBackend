package com.saraasansor.api.dto;

import com.saraasansor.api.model.Elevator;

import java.time.LocalDate;

public class ElevatorDto {
    private Long id;
    private String identityNumber;
    private String buildingName;
    private String address;
    private String elevatorNumber;
    private Integer floorCount;
    private Integer capacity;
    private Double speed;
    private String technicalNotes;
    private String driveType;
    private String machineBrand;
    private String doorType;
    private Integer installationYear;
    private String serialNumber;
    private String controlSystem;
    private String rope;
    private String modernization;
    private LocalDate inspectionDate;
    private LocalDate expiryDate;

    public ElevatorDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getElevatorNumber() {
        return elevatorNumber;
    }

    public void setElevatorNumber(String elevatorNumber) {
        this.elevatorNumber = elevatorNumber;
    }

    public Integer getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(Integer floorCount) {
        this.floorCount = floorCount;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getTechnicalNotes() {
        return technicalNotes;
    }

    public void setTechnicalNotes(String technicalNotes) {
        this.technicalNotes = technicalNotes;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getMachineBrand() {
        return machineBrand;
    }

    public void setMachineBrand(String machineBrand) {
        this.machineBrand = machineBrand;
    }

    public String getDoorType() {
        return doorType;
    }

    public void setDoorType(String doorType) {
        this.doorType = doorType;
    }

    public Integer getInstallationYear() {
        return installationYear;
    }

    public void setInstallationYear(Integer installationYear) {
        this.installationYear = installationYear;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getControlSystem() {
        return controlSystem;
    }

    public void setControlSystem(String controlSystem) {
        this.controlSystem = controlSystem;
    }

    public String getRope() {
        return rope;
    }

    public void setRope(String rope) {
        this.rope = rope;
    }

    public String getModernization() {
        return modernization;
    }

    public void setModernization(String modernization) {
        this.modernization = modernization;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public static ElevatorDto fromEntity(Elevator elevator) {
        ElevatorDto dto = new ElevatorDto();
        dto.setId(elevator.getId());
        dto.setIdentityNumber(elevator.getIdentityNumber());
        dto.setBuildingName(elevator.getBuildingName());
        dto.setAddress(elevator.getAddress());
        dto.setElevatorNumber(elevator.getElevatorNumber());
        dto.setFloorCount(elevator.getFloorCount());
        dto.setCapacity(elevator.getCapacity());
        dto.setSpeed(elevator.getSpeed());
        dto.setTechnicalNotes(elevator.getTechnicalNotes());
        dto.setDriveType(elevator.getDriveType());
        dto.setMachineBrand(elevator.getMachineBrand());
        dto.setDoorType(elevator.getDoorType());
        dto.setInstallationYear(elevator.getInstallationYear());
        dto.setSerialNumber(elevator.getSerialNumber());
        dto.setControlSystem(elevator.getControlSystem());
        dto.setRope(elevator.getRope());
        dto.setModernization(elevator.getModernization());
        dto.setInspectionDate(elevator.getInspectionDate());
        dto.setExpiryDate(elevator.getExpiryDate());
        return dto;
    }
}
