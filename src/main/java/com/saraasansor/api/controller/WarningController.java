package com.saraasansor.api.controller;

import com.saraasansor.api.dto.ApiResponse;
import com.saraasansor.api.dto.ElevatorDto;
import com.saraasansor.api.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warnings")
public class WarningController {
    
    @Autowired
    private ElevatorService elevatorService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ElevatorDto>>> getWarnings(
            @RequestParam(required = false) String type) {
        List<ElevatorDto> elevators;
        
        if ("EXPIRED".equalsIgnoreCase(type)) {
            elevators = elevatorService.getExpiredElevators();
        } else if ("WARNING".equalsIgnoreCase(type)) {
            elevators = elevatorService.getExpiringSoonElevators();
        } else {
            // Return both
            elevators = elevatorService.getExpiredElevators();
            elevators.addAll(elevatorService.getExpiringSoonElevators());
        }
        
        return ResponseEntity.ok(ApiResponse.success(elevators));
    }
}

