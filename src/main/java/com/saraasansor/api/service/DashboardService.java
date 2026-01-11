package com.saraasansor.api.service;

import com.saraasansor.api.dto.DashboardSummaryDto;
import com.saraasansor.api.repository.ElevatorRepository;
import com.saraasansor.api.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {
    
    @Autowired
    private ElevatorRepository elevatorRepository;
    
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    
    @Autowired
    private MaintenanceService maintenanceService;
    
    public DashboardSummaryDto getSummary() {
        DashboardSummaryDto summary = new DashboardSummaryDto();
        
        summary.setTotalElevators(elevatorRepository.count());
        summary.setTotalMaintenances(maintenanceRepository.count());
        
        // Calculate total income (paid maintenances)
        Double totalIncome = maintenanceRepository.findAll().stream()
                .filter(m -> m.getIsPaid() != null && m.getIsPaid() && m.getAmount() != null)
                .mapToDouble(m -> m.getAmount())
                .sum();
        summary.setTotalIncome(totalIncome);
        
        // Calculate total debt (unpaid maintenances)
        Double totalDebt = maintenanceRepository.findAll().stream()
                .filter(m -> m.getIsPaid() == null || !m.getIsPaid())
                .filter(m -> m.getAmount() != null)
                .mapToDouble(m -> m.getAmount())
                .sum();
        summary.setTotalDebt(totalDebt);
        
        // Count expired elevators
        summary.setExpiredCount((long) elevatorRepository.findExpiredElevators(LocalDate.now()).size());
        
        // Count warning elevators
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysLater = now.plusDays(30);
        summary.setWarningCount((long) elevatorRepository.findExpiringSoonElevators(now, thirtyDaysLater).size());
        
        return summary;
    }
}
