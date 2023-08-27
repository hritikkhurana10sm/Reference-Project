package com.amanboora.parking.repository;

import com.amanboora.parking.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRepo extends JpaRepository<Parking, String> {

    Optional<List<Parking>> findByUserId(String userId);

    Optional<Parking> findByIdAndUserId(String id, String userId);

    boolean existsByIdAndUserId(String id, String userId);
}
