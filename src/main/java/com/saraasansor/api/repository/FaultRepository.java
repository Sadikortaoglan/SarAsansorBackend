package com.saraasansor.api.repository;

import com.saraasansor.api.model.Fault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaultRepository extends JpaRepository<Fault, Long> {
    List<Fault> findByElevatorIdOrderByCreatedAtDesc(Long elevatorId);
    List<Fault> findByStatusOrderByCreatedAtDesc(Fault.Status status);
    List<Fault> findByElevatorIdAndStatusOrderByCreatedAtDesc(Long elevatorId, Fault.Status status);
}

