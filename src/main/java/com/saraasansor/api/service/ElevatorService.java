package com.saraasansor.api.service;

import com.saraasansor.api.dto.ElevatorDto;
import com.saraasansor.api.dto.ElevatorStatusDto;
import com.saraasansor.api.model.Elevator;
import com.saraasansor.api.repository.ElevatorRepository;
import com.saraasansor.api.util.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ElevatorService {
    
    @Autowired
    private ElevatorRepository elevatorRepository;
    
    @Autowired
    private AuditLogger auditLogger;
    
    public List<ElevatorDto> getAllElevators() {
        return elevatorRepository.findAll().stream()
                .map(ElevatorDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public ElevatorDto getElevatorById(Long id) {
        Elevator elevator = elevatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Elevator not found"));
        return ElevatorDto.fromEntity(elevator);
    }
    
    public ElevatorDto createElevator(ElevatorDto dto) {
        if (elevatorRepository.existsByIdentityNumber(dto.getIdentityNumber())) {
            throw new RuntimeException("This identity number is already in use");
        }
        
        Elevator elevator = new Elevator();
        mapDtoToEntity(dto, elevator);
        
        // Calculate expiryDate = inspectionDate + 12 months
        LocalDate oldExpiryDate = elevator.getExpiryDate();
        if (elevator.getInspectionDate() != null) {
            elevator.setExpiryDate(elevator.getInspectionDate().plusMonths(12));
        }
        
        Elevator saved = elevatorRepository.save(elevator);
        
        // Log periodic date update
        if (oldExpiryDate == null || !oldExpiryDate.equals(saved.getExpiryDate())) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("inspectionDate", saved.getInspectionDate());
            metadata.put("expiryDate", saved.getExpiryDate());
            auditLogger.log("PERIODIC_DATE_UPDATED", "ELEVATOR", saved.getId(), metadata);
        }
        
        return ElevatorDto.fromEntity(saved);
    }
    
    public ElevatorDto updateElevator(Long id, ElevatorDto dto) {
        Elevator elevator = elevatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Elevator not found"));
        
        // Check identityNumber uniqueness if changed
        if (!elevator.getIdentityNumber().equals(dto.getIdentityNumber()) && 
            elevatorRepository.existsByIdentityNumber(dto.getIdentityNumber())) {
            throw new RuntimeException("This identity number is already in use");
        }
        
        LocalDate oldInspectionDate = elevator.getInspectionDate();
        LocalDate oldExpiryDate = elevator.getExpiryDate();
        
        mapDtoToEntity(dto, elevator);
        
        // Calculate expiryDate = inspectionDate + 12 months
        if (elevator.getInspectionDate() != null) {
            elevator.setExpiryDate(elevator.getInspectionDate().plusMonths(12));
        }
        
        Elevator saved = elevatorRepository.save(elevator);
        
        // Log periodic date update if changed
        if (oldInspectionDate == null || !oldInspectionDate.equals(saved.getInspectionDate()) ||
            oldExpiryDate == null || !oldExpiryDate.equals(saved.getExpiryDate())) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("inspectionDate", saved.getInspectionDate());
            metadata.put("expiryDate", saved.getExpiryDate());
            auditLogger.log("PERIODIC_DATE_UPDATED", "ELEVATOR", saved.getId(), metadata);
        }
        
        return ElevatorDto.fromEntity(saved);
    }
    
    public void deleteElevator(Long id) {
        if (!elevatorRepository.existsById(id)) {
            throw new RuntimeException("Elevator not found");
        }
        elevatorRepository.deleteById(id);
    }
    
    public ElevatorStatusDto getElevatorStatus(Long id) {
        Elevator elevator = elevatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Elevator not found"));
        
        LocalDate now = LocalDate.now();
        LocalDate expiryDate = elevator.getExpiryDate();
        
        ElevatorStatusDto status = new ElevatorStatusDto();
        status.setIdentityNumber(elevator.getIdentityNumber());
        status.setBuildingName(elevator.getBuildingName());
        status.setExpiryDate(expiryDate);
        
        long daysLeft = ChronoUnit.DAYS.between(now, expiryDate);
        status.setDaysLeft(daysLeft);
        
        if (now.isAfter(expiryDate)) {
            status.setStatus("EXPIRED");
        } else if (daysLeft <= 30) {
            status.setStatus("WARNING");
        } else {
            status.setStatus("OK");
        }
        
        return status;
    }
    
    public List<ElevatorDto> getExpiredElevators() {
        return elevatorRepository.findExpiredElevators(LocalDate.now()).stream()
                .map(ElevatorDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<ElevatorDto> getExpiringSoonElevators() {
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysLater = now.plusDays(30);
        return elevatorRepository.findExpiringSoonElevators(now, thirtyDaysLater).stream()
                .map(ElevatorDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    private void mapDtoToEntity(ElevatorDto dto, Elevator entity) {
        entity.setIdentityNumber(dto.getIdentityNumber());
        entity.setBuildingName(dto.getBuildingName());
        entity.setAddress(dto.getAddress());
        entity.setElevatorNumber(dto.getElevatorNumber());
        entity.setFloorCount(dto.getFloorCount());
        entity.setCapacity(dto.getCapacity());
        entity.setSpeed(dto.getSpeed());
        entity.setTechnicalNotes(dto.getTechnicalNotes());
        entity.setDriveType(dto.getDriveType());
        entity.setMachineBrand(dto.getMachineBrand());
        entity.setDoorType(dto.getDoorType());
        entity.setInstallationYear(dto.getInstallationYear());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setControlSystem(dto.getControlSystem());
        entity.setRope(dto.getRope());
        entity.setModernization(dto.getModernization());
        entity.setInspectionDate(dto.getInspectionDate());
        // expiryDate will be calculated
    }
}
