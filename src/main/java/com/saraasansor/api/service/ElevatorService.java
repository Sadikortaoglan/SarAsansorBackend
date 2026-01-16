package com.saraasansor.api.service;

import com.saraasansor.api.dto.ElevatorDto;
import com.saraasansor.api.dto.ElevatorStatusDto;
import com.saraasansor.api.dto.WarningDto;
import com.saraasansor.api.dto.WarningElevatorDto;
import com.saraasansor.api.dto.WarningGroupDto;
import com.saraasansor.api.model.Elevator;
import com.saraasansor.api.repository.ElevatorRepository;
import com.saraasansor.api.util.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        return elevatorRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
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
        
        // Log before mapping
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ElevatorService.class);
        log.info(
            "Updating elevator: floorCount={}, capacity={}, speed={}, inspectionDate={}, blueLabel={}",
            dto.getFloorCount(),
            dto.getCapacity(),
            dto.getSpeed(),
            dto.getInspectionDate(),
            dto.getBlueLabel()
        );
        
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
    
    /**
     * Get expired elevators as WarningDto with complete information
     * Returns elevators with identityNo, buildingName, address, maintenanceEndDate, and status
     */
    public List<WarningDto> getExpiredElevatorsAsWarnings() {
        LocalDate now = LocalDate.now();
        return elevatorRepository.findExpiredElevators(now).stream()
                .map(elevator -> WarningDto.fromEntity(elevator, "EXPIRED"))
                .collect(Collectors.toList());
    }
    
    /**
     * Get expiring soon elevators as WarningDto with complete information
     * Returns elevators with identityNo, buildingName, address, maintenanceEndDate, and status
     */
    public List<WarningDto> getExpiringSoonElevatorsAsWarnings() {
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysLater = now.plusDays(30);
        return elevatorRepository.findExpiringSoonElevators(now, thirtyDaysLater).stream()
                .map(elevator -> WarningDto.fromEntity(elevator, "WARNING"))
                .collect(Collectors.toList());
    }
    
    /**
     * Get warnings grouped by building (buildingName + address)
     * Groups elevators by building and returns structured data for frontend expansion
     * 
     * Sorting:
     * - EXPIRED buildings first
     * - Then WARNING buildings
     * - Within each group, elevators sorted by maintenanceEndDate ASC
     * 
     * @param type Optional: "EXPIRED", "WARNING", or null (both)
     * @return List of WarningGroupDto grouped by building
     */
    public List<WarningGroupDto> getGroupedWarnings(String type) {
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysLater = now.plusDays(30);
        
        List<Elevator> expiredElevators = new ArrayList<>();
        List<Elevator> warningElevators = new ArrayList<>();
        
        if (type == null || "EXPIRED".equalsIgnoreCase(type)) {
            expiredElevators = elevatorRepository.findExpiredElevators(now);
        }
        
        if (type == null || "WARNING".equalsIgnoreCase(type)) {
            warningElevators = elevatorRepository.findExpiringSoonElevators(now, thirtyDaysLater);
        }
        
        // Group expired elevators by building (buildingName + address)
        Map<String, WarningGroupDto> expiredGroups = expiredElevators.stream()
                .collect(Collectors.groupingBy(
                    elevator -> buildGroupKey(elevator.getBuildingName(), elevator.getAddress()),
                    LinkedHashMap::new,
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        elevators -> {
                            Elevator first = elevators.get(0);
                            List<WarningElevatorDto> elevatorDtos = elevators.stream()
                                    .sorted(Comparator.comparing(Elevator::getExpiryDate))
                                    .map(e -> new WarningElevatorDto(
                                        e.getIdentityNumber() != null ? e.getIdentityNumber() : "",
                                        e.getExpiryDate(),
                                        "EXPIRED"
                                    ))
                                    .collect(Collectors.toList());
                            
                            return new WarningGroupDto(
                                first.getBuildingName() != null ? first.getBuildingName() : "",
                                first.getAddress() != null ? first.getAddress() : "",
                                "EXPIRED",
                                elevatorDtos
                            );
                        }
                    )
                ));
        
        // Group warning elevators by building (buildingName + address)
        Map<String, WarningGroupDto> warningGroups = warningElevators.stream()
                .collect(Collectors.groupingBy(
                    elevator -> buildGroupKey(elevator.getBuildingName(), elevator.getAddress()),
                    LinkedHashMap::new,
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        elevators -> {
                            Elevator first = elevators.get(0);
                            List<WarningElevatorDto> elevatorDtos = elevators.stream()
                                    .sorted(Comparator.comparing(Elevator::getExpiryDate))
                                    .map(e -> new WarningElevatorDto(
                                        e.getIdentityNumber() != null ? e.getIdentityNumber() : "",
                                        e.getExpiryDate(),
                                        "WARNING"
                                    ))
                                    .collect(Collectors.toList());
                            
                            return new WarningGroupDto(
                                first.getBuildingName() != null ? first.getBuildingName() : "",
                                first.getAddress() != null ? first.getAddress() : "",
                                "WARNING",
                                elevatorDtos
                            );
                        }
                    )
                ));
        
        // Combine and sort: EXPIRED first, then WARNING
        List<WarningGroupDto> result = new ArrayList<>();
        result.addAll(expiredGroups.values());
        result.addAll(warningGroups.values());
        
        return result;
    }
    
    /**
     * Build a unique key for grouping by buildingName and address
     */
    private String buildGroupKey(String buildingName, String address) {
        String name = buildingName != null ? buildingName : "";
        String addr = address != null ? address : "";
        return name + "|" + addr; // Use pipe separator to combine
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
        entity.setBlueLabel(dto.getBlueLabel());
        // expiryDate will be calculated from inspectionDate
    }
}
