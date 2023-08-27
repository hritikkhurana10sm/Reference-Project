package com.amanboora.parking.dao;

import com.amanboora.parking.dto.ParkingSpotDto;

import java.util.List;

public interface ParkingSpotService {

    List<ParkingSpotDto> getSpotsByParking(String parkingId);

    List<ParkingSpotDto> addSpots(List<ParkingSpotDto> parkingSpots, String parkingId, String parentBlockId, String location);

    List<ParkingSpotDto> updateSpots(String parkingId, String parentBlockId, List<ParkingSpotDto> parkingSpots, String location);

    ParkingSpotDto spotAllocation(String id, String carNum);

    ParkingSpotDto automatedSpotAllocation(String parkingId, String carNum);

    ParkingSpotDto spotDeallocation(String carNum);

}
