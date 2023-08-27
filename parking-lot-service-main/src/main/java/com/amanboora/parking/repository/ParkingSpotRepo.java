package com.amanboora.parking.repository;

import com.amanboora.parking.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepo extends JpaRepository<ParkingSpot, String> {

    Optional<List<ParkingSpot>> findByParkingId(String parkingId);

    Optional<List<ParkingSpot>> findByParentBlockId(String parentBlockId);

    @Query("Select PS from ParkingSpot PS INNER JOIN Parking P ON P.id=PS.parkingId And P.userId=:userId And PS.id=:id")
    Optional<ParkingSpot> findByIdAndUserId(@Param("id") String id, @Param("userId") String userId);

    @Query("Select PS from ParkingSpot PS INNER JOIN Parking P ON P.id=PS.parkingId And P.userId=:userId And PS.carNum=:carNum")
    Optional<ParkingSpot> findByCarNumberAndUserId(@Param("carNum") String carNum, @Param("userId") String userId);

    @Query("SELECT CASE WHEN EXISTS ( " +
            "Select 1 from ParkingSpot carSpot " +
            "INNER JOIN Parking P ON P.userId=:userId And carSpot.carNum=:carNum " +
            "AND P.id = (SELECT parkingId from ParkingSpot WHERE id = :parkingSpotId)" +
            ") THEN true ELSE false END ")
    boolean existsByCarNumberBySpotId(@Param("parkingSpotId") String parkingSpotId, @Param("carNum") String carNum, @Param("userId") String userId);

    @Query("SELECT CASE WHEN EXISTS ( " +
            "Select 1 from ParkingSpot carSpot " +
            "INNER JOIN Parking P ON P.userId=:userId And carSpot.carNum=:carNum " +
            "AND P.id = :parkingId" +
            ") THEN true ELSE false END ")
    boolean existsByCarNumberByParkingId(@Param("parkingId") String parkingId, @Param("carNum") String carNum, @Param("userId") String userId);

    @Modifying
    @Query(value = " WITH emptyspots AS (" +
            "   SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS rowNumber " +
            "   FROM parking_spot " +
            "   WHERE parking_id = :parkingId AND car_number IS NULL) " +
            " UPDATE parking_spot " +
            " SET car_number = :carNum " +
            " WHERE id IN (SELECT id FROM emptyspots WHERE rowNumber = 1)", nativeQuery = true)
    Optional<Integer> updateAvailableSpotByParkingIdAndUserId(@Param("parkingId") String parkingId, @Param("carNum") String carNum);

    @Query(value = "SELECT PS from ParkingSpot PS INNER JOIN Parking P ON P.id=PS.parkingId AND PS.parkingId = :parkingId AND P.userId = :userId WHERE PS.parkingId=:parkingId AND PS.carNum=:carNum")
    Optional<ParkingSpot> findAllottedSpotByCarNumAndParkingId(@Param("parkingId") String parkingId, @Param("carNum") String carNum,@Param("userId") String userId);
}