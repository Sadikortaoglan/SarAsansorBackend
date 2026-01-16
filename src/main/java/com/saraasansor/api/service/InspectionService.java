package com.saraasansor.api.service;

import com.saraasansor.api.dto.InspectionDto;
import com.saraasansor.api.model.Elevator;
import com.saraasansor.api.model.Inspection;
import com.saraasansor.api.repository.ElevatorRepository;
import com.saraasansor.api.repository.InspectionRepository;
import com.saraasansor.api.util.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class InspectionService {
    
    @Autowired
    private InspectionRepository inspectionRepository;
    
    @Autowired
    private ElevatorRepository elevatorRepository;
    
    @Autowired
    private AuditLogger auditLogger;
    
    public List<InspectionDto> getAllInspections() {
        return inspectionRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(InspectionDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<InspectionDto> getInspectionsByElevatorId(Long elevatorId) {
        return inspectionRepository.findByElevatorIdOrderByDateDesc(elevatorId).stream()
                .map(InspectionDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public InspectionDto createInspection(InspectionDto dto) {
        Elevator elevator = elevatorRepository.findById(dto.getElevatorId())
                .orElseThrow(() -> new RuntimeException("Elevator not found"));
        
        Inspection inspection = new Inspection();
        inspection.setElevator(elevator);
        inspection.setDate(dto.getDate());
        inspection.setResult(dto.getResult());
        inspection.setDescription(dto.getDescription());
        
        Inspection saved = inspectionRepository.save(inspection);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("elevatorId", saved.getElevator().getId());
        metadata.put("date", saved.getDate());
        metadata.put("result", saved.getResult());
        auditLogger.log("INSPECTION_CREATED", "INSPECTION", saved.getId(), metadata);
        
        return InspectionDto.fromEntity(saved);
    }
    
    public InspectionDto getInspectionById(Long id) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection record not found"));
        return InspectionDto.fromEntity(inspection);
    }
    
    public InspectionDto updateInspection(Long id, InspectionDto dto) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection record not found"));
        
        Elevator elevator = elevatorRepository.findById(dto.getElevatorId())
                .orElseThrow(() -> new RuntimeException("Elevator not found"));
        
        inspection.setElevator(elevator);
        inspection.setDate(dto.getDate());
        inspection.setResult(dto.getResult());
        inspection.setDescription(dto.getDescription());
        
        Inspection saved = inspectionRepository.save(inspection);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("elevatorId", saved.getElevator().getId());
        metadata.put("date", saved.getDate());
        metadata.put("result", saved.getResult());
        auditLogger.log("INSPECTION_UPDATED", "INSPECTION", saved.getId(), metadata);
        
        return InspectionDto.fromEntity(saved);
    }
    
    public void deleteInspection(Long id) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection record not found"));
        
        inspectionRepository.delete(inspection);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("date", inspection.getDate());
        metadata.put("result", inspection.getResult());
        auditLogger.log("INSPECTION_DELETED", "INSPECTION", id, metadata);
    }
}
