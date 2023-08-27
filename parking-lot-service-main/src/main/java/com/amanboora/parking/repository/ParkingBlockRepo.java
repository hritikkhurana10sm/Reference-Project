package com.amanboora.parking.repository;

import com.amanboora.parking.model.ParkingBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingBlockRepo extends JpaRepository<ParkingBlock, String> {

    @Query("Select PB from ParkingBlock PB INNER JOIN Parking P ON P.id=PB.parkingId And P.userId=:userId And PB.id=:id")
    Optional<ParkingBlock> findByIdAndUserId(@Param("id") String id, @Param("userId") String userId);

    Optional<List<ParkingBlock>> findByParkingId(String parkingId);
}
