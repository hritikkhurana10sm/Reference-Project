package com.amanboora.parking.services;

import com.amanboora.parking.Exception.ResourceNotFound;
import com.amanboora.parking.dao.ParkingBlockService;
import com.amanboora.parking.dao.ParkingSpotService;
import com.amanboora.parking.dto.ParkingBlockDto;
import com.amanboora.parking.dto.ParkingDto;
import com.amanboora.parking.dto.ParkingSpotDto;
import com.amanboora.parking.model.Parking;
import com.amanboora.parking.repository.ParkingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl implements com.amanboora.parking.dao.ParkingService {

    @Autowired
    private UserServices userServices;
    @Autowired
    private ParkingBlockService parkingBlockService;
    @Autowired
    private ParkingSpotService parkingSpotService;
    @Autowired
    private ParkingRepo parkingRepo;

    public ParkingServiceImpl() {
    }

    @Override
    public boolean existByParkingIdAndUserId(String id) {
        return parkingRepo.existsByIdAndUserId(id, userServices.getUserId());
    }

    @Override
    public ParkingDto getParking(String id) {
        Parking parking = parkingRepo.findByIdAndUserId(id, userServices.getUserId())
                .orElseThrow(() -> new ResourceNotFound("No Content Found! Parking is not configured"));
        ParkingDto response = new ParkingDto(parking.getId(), parking.getParkingName(), parking.getParkingStatus());
        List<ParkingBlockDto> blocks = parkingBlockService.getBlocksByParking(id);
        List<ParkingSpotDto> spots = parkingSpotService.getSpotsByParking(id);
        if (!spots.isEmpty())
            spots.sort(Comparator.comparing(ParkingSpotDto::getSequence));
        if (blocks.isEmpty()) {
            response.setSpots(spots);
            return response;
        } else
            blocks.sort(Comparator.comparing(ParkingBlockDto::getSequence));
        Map<String, List<ParkingSpotDto>> spotsByParking = spots.stream()
                .collect(Collectors.groupingBy(ParkingSpotDto::getParentBlockId));
        for (ParkingBlockDto pb : blocks) {
            if (spotsByParking.containsKey(pb.getId())) {
                List<ParkingSpotDto> spotsInBlock = spotsByParking.get(pb.getId());
                spotsInBlock.sort(Comparator.comparing(ParkingSpotDto::getSequence));
                pb.setSpots(spotsInBlock);
            }
        }
        Map<String, List<ParkingBlockDto>> blocksByParking = blocks.stream().collect(Collectors.groupingBy(parkingBlockDto -> {
            if (parkingBlockDto.getParentBlockId() == null) {
                return "root";
            } else {
                return parkingBlockDto.getParentBlockId();
            }
        }));
        for (ParkingBlockDto pb : blocks) {
            if (blocksByParking.containsKey(pb.getId())) {
                List<ParkingBlockDto> blocksInBlock = blocksByParking.get(pb.getId());
                blocksInBlock.sort(Comparator.comparing(ParkingBlockDto::getSequence));
                pb.setBlocks(blocksInBlock);
            }
        }
        List<ParkingBlockDto> rootBlocks = blocksByParking.get("root");
        rootBlocks.sort(Comparator.comparing(ParkingBlockDto::getSequence));
        response.setBlocks(rootBlocks);
        return response;
    }

    @Override
    public List<ParkingDto> getParkingsByUser() {
        List<Parking> parkingByUser = parkingRepo.findByUserId(userServices.getUserId())
                .orElseThrow(() -> new ResourceNotFound("No Parking Found or Authorized!"));
        List<ParkingDto> parkingsByUser = new ArrayList<>();
        for (Parking parking : parkingByUser) {
            ParkingDto parkingDto = new ParkingDto(parking.getId(), parking.getParkingName(), parking.getParkingStatus());
            parkingsByUser.add(parkingDto);
        }
        parkingsByUser.sort(Comparator.comparing(ParkingDto::getStatus));
        return parkingsByUser;
    }

    @Override
    public ParkingDto registerParking(ParkingDto parking) {
        if (parking.getName() == null || parking.getName().isEmpty()) {
            throw new RuntimeException("Parking without name cannot be registered!");
        }
        if (!Objects.equals(parking.getStatus(), "Draft")) {
            throw new RuntimeException("Parking can only be registered with Draft Status!");
        }
        UUID id = UUID.randomUUID();
        Parking singleParking = new Parking(id.toString(), userServices.getUserId(), parking.getName(), "Draft");
        Parking savedParking = parkingRepo.save(singleParking);
        List<ParkingBlockDto> responseBlocks = new ArrayList<>();
        List<ParkingSpotDto> responseSpots = new ArrayList<>();
        if (parking.getSpots() != null && !parking.getSpots().isEmpty() && parking.getBlocks() != null && !parking.getBlocks().isEmpty()) {
            throw new RuntimeException("Parking Cannot have both spots and blocks at same level!");
        }
        if (parking.getSpots() != null && !parking.getSpots().isEmpty()) {
            responseSpots = parkingSpotService.addSpots(parking.getSpots(), savedParking.getId(), null, "");
        } else if(parking.getBlocks() != null && !parking.getBlocks().isEmpty()){
            ParkingBlockDto rootBlock = new ParkingBlockDto();
            rootBlock.setId(null);
            rootBlock.setParkingId(savedParking.getId());
            rootBlock.setBlocks(parking.getBlocks());
            responseBlocks = parkingBlockService.addBlocks(savedParking.getId(), rootBlock);
        }
        return new ParkingDto(savedParking.getId(), savedParking.getParkingName(), savedParking.getParkingStatus(), responseBlocks, responseSpots);
    }

    @Override
    public ParkingDto updateParking(String id, ParkingDto parking) {
        if (Objects.equals(parking.getStatus(), "Active")) {
            throw new RuntimeException("Parking cannot be updated in active state!");
        }
        Parking parkingInDb = parkingRepo.findByIdAndUserId(id, userServices.getUserId())
                .orElseThrow(() -> new ResourceNotFound("No Parking Found with id: " + id));
        if (parking.getSpots() != null && !parking.getSpots().isEmpty() && parking.getBlocks() != null && !parking.getBlocks().isEmpty()) {
            throw new RuntimeException("Parking Cannot have both spots and blocks at same level!");
        }
        parkingInDb.setParkingName(parking.getName());
        parkingInDb.setParkingStatus(parking.getStatus());
        Parking updatedParking = parkingRepo.save(parkingInDb);
        List<ParkingBlockDto> responseBlocks = new ArrayList<>();
        List<ParkingSpotDto> responseSpots = new ArrayList<>();
        if (parking.getSpots() != null && !parking.getSpots().isEmpty()) {
            responseSpots = parkingSpotService.updateSpots(id, null, parking.getSpots(), "");
        } else {
            ParkingBlockDto rootBlock = new ParkingBlockDto();
            rootBlock.setId(null);
            rootBlock.setParkingId(id);
            rootBlock.setBlocks(parking.getBlocks());
            responseBlocks = parkingBlockService.updateBlocks(id, rootBlock);
        }
        return new ParkingDto(updatedParking.getId(), updatedParking.getParkingName(),
                updatedParking.getParkingStatus(), responseBlocks, responseSpots);
    }

    @Override
    public ParkingDto updateParkingStatus(String id, String status) {
        Parking parkingInDb = parkingRepo.findByIdAndUserId(id, userServices.getUserId())
                .orElseThrow(() -> new ResourceNotFound("Unauthorized access for a parking with id: " + id));
        String currentStatus = parkingInDb.getParkingStatus();
        if (Objects.equals(status, "Draft")) {
            throw new RuntimeException("Parking status cannot be changed to Draft status!");
        }
        if (Objects.equals(status, "Active") && Objects.equals(currentStatus, "Active")) {
            throw new RuntimeException("Active parking can only be changed to Inactive & Draft state!");
        }
        if (Objects.equals(status, "Inactive") && !Objects.equals(currentStatus, "Active")) {
            throw new RuntimeException("Inactive parking can only be changed to Active state!");
        }
        parkingInDb.setParkingStatus(status);
        parkingRepo.save(parkingInDb);
        return new ParkingDto(id, parkingInDb.getParkingName(), parkingInDb.getParkingStatus());
    }
}
