package com.amanboora.parking.services;

import com.amanboora.parking.Exception.DataInvalidationException;
import com.amanboora.parking.Exception.ParkingException;
import com.amanboora.parking.Exception.ResourceNotFound;
import com.amanboora.parking.dto.ParkingSpotDto;
import com.amanboora.parking.model.Parking;
import com.amanboora.parking.model.ParkingSpot;
import com.amanboora.parking.repository.ParkingRepo;
import com.amanboora.parking.repository.ParkingSpotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParkingSpotServiceImpl implements com.amanboora.parking.dao.ParkingSpotService {

    @Autowired
    private ParkingSpotRepo parkingSpotRepo;
    @Autowired
    private ParkingRepo parkingRepo;
    @Autowired
    private UserServices userServices;

    public ParkingSpotServiceImpl() {
    }

    @Override
    public List<ParkingSpotDto> getSpotsByParking(String parkingId) {
        return parkingSpotRepo.findByParkingId(parkingId).orElse(new ArrayList<>()).stream().map(ps -> new ParkingSpotDto(ps.getId(), ps.getParkingId(), ps.getParentBlockId(), ps.getSpotName(), ps.getSpotSequence(), ps.getLocation(), ps.getCarNum())).collect(Collectors.toList());
    }

    @Override
    public List<ParkingSpotDto> addSpots(List<ParkingSpotDto> rqSpots, String parkingId, String parentBlockId, String location) {
        List<ParkingSpotDto> parkingSpotsDto = new ArrayList<>();
        if (rqSpots != null) {
            for (ParkingSpotDto ps : rqSpots) {
                String spotLocation = location + " " + ps.getName();
                ps.setParkingId(parkingId);
                ps.setParentBlockId(parentBlockId);
                ParkingSpot savedSpot = parkingSpotRepo.save(new ParkingSpot(UUID.randomUUID().toString(), ps.getParkingId(), ps.getParentBlockId(), ps.getSequence(), ps.getName(), spotLocation));
                ps.setId(savedSpot.getId());
                parkingSpotsDto.add(ps);
            }
        }
        return parkingSpotsDto;
    }

    @Override
    public List<ParkingSpotDto> updateSpots(String parkingId, String parentBlockId, List<ParkingSpotDto> parkingSpots, String location) {
        List<ParkingSpotDto> updatedSpots = new ArrayList<>();
        Map<String, Boolean> presentInDb = new HashMap<>();
        if (parkingSpots != null && !parkingSpots.isEmpty()) {
            for (ParkingSpotDto ps : parkingSpots) {
                String spotLocation = location + " " + ps.getName();
                if (ps.getId() == null) {
                    ParkingSpot newSpot = parkingSpotRepo.save(new ParkingSpot(UUID.randomUUID().toString(), parkingId, parentBlockId, ps.getSequence(), ps.getName(), spotLocation));
                    presentInDb.put(newSpot.getId(), true);
                    updatedSpots.add(new ParkingSpotDto(newSpot.getId(), newSpot.getParkingId(), newSpot.getParentBlockId(), newSpot.getSpotName(), newSpot.getSpotSequence(), newSpot.getLocation()));
                } else {
                    ParkingSpot parkingSpotInDb = parkingSpotRepo.findByIdAndUserId(ps.getId(), userServices.getUserId()).orElseThrow(() -> new ResourceNotFound("No spot found with id: " + ps.getId()));
                    parkingSpotInDb.setSpotName(ps.getName());
                    parkingSpotInDb.setSpotSequence(ps.getSequence());
                    parkingSpotRepo.save(parkingSpotInDb);
                    presentInDb.put(parkingSpotInDb.getId(), true);
                    updatedSpots.add(new ParkingSpotDto(parkingSpotInDb.getId(), parkingSpotInDb.getParkingId(), parkingSpotInDb.getParentBlockId(), parkingSpotInDb.getSpotName(), parkingSpotInDb.getSpotSequence(), parkingSpotInDb.getLocation()));
                }
            }
        }
        List<ParkingSpot> spotsInBlock = parkingSpotRepo.findByParentBlockId(parentBlockId).orElse(new ArrayList<>());
        List<ParkingSpot> toBeDeleteSpots = new ArrayList<>();
        for (ParkingSpot spot : spotsInBlock) {
            if (!presentInDb.containsKey(spot.getId())) {
                toBeDeleteSpots.add(spot);
            }
        }
        parkingSpotRepo.deleteAll(toBeDeleteSpots);
        return updatedSpots;
    }

    @Transactional
    @Override
    public ParkingSpotDto spotAllocation(String id, String carNum) {
        if (parkingSpotRepo.existsByCarNumberBySpotId(id,carNum, userServices.getUserId()))
            throw new DataInvalidationException("Vehicle with number " + carNum + " is already parked in a parking!");
        ParkingSpot spotInDb = parkingSpotRepo.findByIdAndUserId(id, userServices.getUserId()).orElseThrow(() -> new ResourceNotFound("Requested spot is not found in the authorized parking!"));
        Parking parkingInDb = parkingRepo.findByIdAndUserId(spotInDb.getParkingId(), userServices.getUserId()).orElseThrow(() -> new ResourceNotFound("No parking found!"));
        if (spotInDb.getCarNum() != null && !spotInDb.getCarNum().isEmpty())
            throw new DataInvalidationException("A Vehicle is already parked at the spot: " + spotInDb.getSpotName());
        if (Objects.equals(parkingInDb.getParkingStatus(), "Active")) {
            spotInDb.setCarNum(carNum);
            parkingSpotRepo.save(spotInDb);
            return new ParkingSpotDto(spotInDb.getId(), spotInDb.getSpotName(), spotInDb.getSpotSequence(), spotInDb.getLocation(), spotInDb.getCarNum());
        } else {
            throw new DataInvalidationException("Vehicle-entry is only allowed in active parking!");
        }
    }

    @Transactional
    @Override
    public ParkingSpotDto automatedSpotAllocation(String parkingId, String carNum) {
        if (parkingSpotRepo.existsByCarNumberByParkingId(parkingId,carNum, userServices.getUserId()))
            throw new DataInvalidationException("Vehicle with number " + carNum + " is already parked in a parking!");
        Parking parkingInDb = parkingRepo.findByIdAndUserId(parkingId, userServices.getUserId()).orElseThrow(() -> new ResourceNotFound("No parking found!"));
        if (Objects.equals(parkingInDb.getParkingStatus(), "Active")) {
            parkingSpotRepo.updateAvailableSpotByParkingIdAndUserId(parkingId, carNum).orElseThrow(()->new ResourceNotFound("Parking is fully occupied!"));
            ParkingSpot spotInDb=parkingSpotRepo.findAllottedSpotByCarNumAndParkingId(parkingId,carNum,userServices.getUserId()).orElseThrow(()-> new ParkingException("Failed to fetch allotted spot", HttpStatus.INTERNAL_SERVER_ERROR));
            return new ParkingSpotDto(spotInDb.getId(), spotInDb.getSpotName(), spotInDb.getSpotSequence(), spotInDb.getLocation(), spotInDb.getCarNum());
        } else {
            throw new DataInvalidationException("Vehicle-entry is only allowed in active parking!");
        }
    }

    @Override
    public ParkingSpotDto spotDeallocation(String carNum) {
        ParkingSpot spotInDb = parkingSpotRepo.findByCarNumberAndUserId(carNum, userServices.getUserId()).orElseThrow(() -> new ResourceNotFound(carNum + " is not found in the authorized parking!"));
        Parking parkingInDb = parkingRepo.findByIdAndUserId(spotInDb.getParkingId(), userServices.getUserId()).orElseThrow(() -> new ResourceNotFound("No parking found!"));
        if (!Objects.equals(parkingInDb.getParkingStatus(), "Draft")) {
            spotInDb.setCarNum(null);
            parkingSpotRepo.save(spotInDb);
            return new ParkingSpotDto(spotInDb.getId(), spotInDb.getSpotName(), spotInDb.getSpotSequence(), spotInDb.getLocation(), spotInDb.getCarNum());
        } else {
            throw new DataInvalidationException("Draft parking cannot have car-exit procedure!");
        }
    }
}
