package com.saraasansor.api.controller;

import com.saraasansor.api.dto.ApiResponse;
import com.saraasansor.api.dto.MaintenanceDto;
import com.saraasansor.api.dto.MaintenanceSummaryDto;
import com.saraasansor.api.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/maintenances")
public class MaintenanceController {
    
    @Autowired
    private MaintenanceService maintenanceService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<MaintenanceDto>>> getAllMaintenances(
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        try {
            List<MaintenanceDto> maintenances;
            
            if (paid != null || dateFrom != null || dateTo != null) {
                maintenances = maintenanceService.getMaintenancesByPaidAndDateRange(paid, dateFrom, dateTo);
            } else {
                maintenances = maintenanceService.getAllMaintenances();
            }
            
            return ResponseEntity.ok(ApiResponse.success(maintenances));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<MaintenanceSummaryDto>> getMonthlySummary(
            @RequestParam(required = false) String month) {
        try {
            MaintenanceSummaryDto summary = maintenanceService.getMonthlySummary(month);
            return ResponseEntity.ok(ApiResponse.success(summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceDto>> getMaintenanceById(@PathVariable Long id) {
        try {
            MaintenanceDto maintenance = maintenanceService.getMaintenanceById(id);
            return ResponseEntity.ok(ApiResponse.success(maintenance));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/elevator/{elevatorId}")
    public ResponseEntity<ApiResponse<List<MaintenanceDto>>> getMaintenancesByElevatorId(
            @PathVariable Long elevatorId) {
        List<MaintenanceDto> maintenances = maintenanceService.getMaintenancesByElevatorId(elevatorId);
        return ResponseEntity.ok(ApiResponse.success(maintenances));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<MaintenanceDto>> createMaintenance(
            @Valid @RequestBody MaintenanceDto dto) {
        try {
            MaintenanceDto created = maintenanceService.createMaintenance(dto);
            return ResponseEntity.ok(ApiResponse.success("Maintenance record successfully added", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceDto>> updateMaintenance(
            @PathVariable Long id, @Valid @RequestBody MaintenanceDto dto) {
        try {
            MaintenanceDto updated = maintenanceService.updateMaintenance(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Maintenance record successfully updated", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMaintenance(@PathVariable Long id) {
        try {
            maintenanceService.deleteMaintenance(id);
            return ResponseEntity.ok(ApiResponse.success("Maintenance record successfully deleted", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/mark-paid")
    public ResponseEntity<ApiResponse<MaintenanceDto>> markPaid(
            @PathVariable Long id, @RequestParam(defaultValue = "true") boolean paid) {
        try {
            MaintenanceDto updated = maintenanceService.markPaid(id, paid);
            return ResponseEntity.ok(ApiResponse.success(
                paid ? "Marked as paid" : "Payment mark removed", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

