package com.amanboora.parking.dao;

import com.amanboora.parking.dto.ParkingDto;

import java.util.List;

public interface ParkingService {

    boolean existByParkingIdAndUserId(String id);

    ParkingDto getParking(String id);

    List<ParkingDto> getParkingsByUser();

    ParkingDto registerParking(ParkingDto parkingDto);

    ParkingDto updateParking(String id,ParkingDto parkingDto);

    ParkingDto updateParkingStatus(String id, String status);
}
