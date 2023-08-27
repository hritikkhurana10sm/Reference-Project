package com.amanboora.parking.dao;

import com.amanboora.parking.dto.ParkingBlockDto;

import java.util.List;

public interface ParkingBlockService {

    List<ParkingBlockDto> getBlocksByParking(String parkingId);

    List<ParkingBlockDto> addBlocks(String parkingId, ParkingBlockDto parkingBlockDto);

    List<ParkingBlockDto> updateBlocks(String parkingId, ParkingBlockDto parkingBlockDto);
}