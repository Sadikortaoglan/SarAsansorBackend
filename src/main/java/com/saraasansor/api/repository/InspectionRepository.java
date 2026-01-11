package com.saraasansor.api.repository;

import com.saraasansor.api.model.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    List<Inspection> findByElevatorIdOrderByDateDesc(Long elevatorId);
}

