package com.saraasansor.api.repository;

import com.saraasansor.api.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT o FROM Offer o WHERE o.elevator.id = :elevatorId")
    List<Offer> findByElevatorId(Long elevatorId);
}

