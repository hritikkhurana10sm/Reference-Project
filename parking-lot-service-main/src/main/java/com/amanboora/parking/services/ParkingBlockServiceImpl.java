package com.amanboora.parking.services;

import com.amanboora.parking.Exception.ResourceNotFound;
import com.amanboora.parking.dao.ParkingSpotService;
import com.amanboora.parking.dto.ParkingBlockDto;
import com.amanboora.parking.dto.ParkingSpotDto;
import com.amanboora.parking.model.ParkingBlock;
import com.amanboora.parking.repository.ParkingBlockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParkingBlockServiceImpl implements com.amanboora.parking.dao.ParkingBlockService {

    @Autowired
    private ParkingBlockRepo parkingBlockRepo;
    @Autowired
    private ParkingSpotService parkingSpotService;
    @Autowired
    private UserServices userServices;

    public ParkingBlockServiceImpl() {
    }

    @Override
    public List<ParkingBlockDto> getBlocksByParking(String parkingId) {
        List<ParkingBlock> parkingBlocks = parkingBlockRepo.findByParkingId(parkingId).orElse(new ArrayList<>());
        List<ParkingBlockDto> resBlocks = new ArrayList<>();
        for (ParkingBlock pb : parkingBlocks) {
            resBlocks.add(new ParkingBlockDto(pb.getId(), pb.getParkingId(), pb.getParentBlockId(), pb.getBlockSequence(), pb.getBlockName()));
        }
        return resBlocks;
    }

    @Override
    public List<ParkingBlockDto> addBlocks(String parkingId, ParkingBlockDto rootBlock) {
        List<ParkingBlockDto> responseBlocks = new ArrayList<>();
        List<ParkingBlockDto> addedBlocks = new ArrayList<>();
        long numOfRootBlocks = rootBlock.getBlocks().size();
        LinkedList<ParkingBlockDto> queue = new LinkedList<>();
        String location = "";
        rootBlock.setLocation(location);
        queue.add(rootBlock);
        while (queue.size() != 0) {
            rootBlock = queue.poll();
            assert rootBlock != null;
            if (rootBlock.getSpots() != null && !rootBlock.getSpots().isEmpty() && rootBlock.getBlocks() != null && !rootBlock.getBlocks().isEmpty()) {
                throw new RuntimeException("Parking Cannot have both spots and blocks at same level!");
            }
            List<ParkingSpotDto> addedSpots;
            if (rootBlock.getSpots() != null && !rootBlock.getSpots().isEmpty()) {
                addedSpots = parkingSpotService.addSpots(rootBlock.getSpots(), parkingId, rootBlock.getId(), rootBlock.getLocation());
                rootBlock.setSpots(addedSpots);
                addedBlocks.add(rootBlock);
            } else {
                if (!Objects.equals(rootBlock.getBlocks(), null)) {
                    for (ParkingBlockDto pb : rootBlock.getBlocks()) {
                        pb.setParkingId(parkingId);
                        pb.setParentBlockId(rootBlock.getId());
                        ParkingBlock blockInDb = parkingBlockRepo.save(new ParkingBlock(UUID.randomUUID().toString(), pb.getParkingId(), pb.getSequence(), pb.getName(), pb.getParentBlockId()));
                        pb.setId(blockInDb.getId());
                        addedBlocks.add(new ParkingBlockDto(blockInDb.getId(), blockInDb.getParkingId(), blockInDb.getParentBlockId(), blockInDb.getBlockSequence(), blockInDb.getBlockName(), pb.getBlocks(), pb.getSpots()));
                        pb.setLocation(rootBlock.getLocation() + " " + pb.getName());
                        queue.add(pb);
                    }
                }
            }
        }
        for (int i = 0; i < numOfRootBlocks; i++) {
            responseBlocks.add(addedBlocks.get(i));
        }
        return responseBlocks;
    }

    @Override
    public List<ParkingBlockDto> updateBlocks(String parkingId, ParkingBlockDto rootBlock) {
        List<ParkingBlockDto> updatedBlocks = new ArrayList<>();
        List<ParkingBlockDto> responseBlocks = new ArrayList<>();
        Map<String, Boolean> presentInBlockDto = new HashMap<>();
        long numOfRootBlocks = rootBlock.getBlocks().size();
        LinkedList<ParkingBlockDto> queue = new LinkedList<>();
        String location = "";
        rootBlock.setLocation(location);
        queue.add(rootBlock);
        while (queue.size() != 0) {
            rootBlock = queue.poll();
            assert rootBlock != null;
            if (rootBlock.getSpots() != null && !rootBlock.getSpots().isEmpty() && rootBlock.getBlocks() != null && !rootBlock.getBlocks().isEmpty()) {
                throw new RuntimeException("Parking Cannot have both spots and blocks at same level!");
            }
            List<ParkingSpotDto> updatedSpots;
            if (rootBlock.getSpots() != null) {
                updatedSpots = parkingSpotService.updateSpots(parkingId, rootBlock.getId(), rootBlock.getSpots(), rootBlock.getLocation());
                rootBlock.setSpots(updatedSpots);
                updatedBlocks.add(rootBlock);
            }
            if (rootBlock.getBlocks() != null && !rootBlock.getBlocks().isEmpty()) {
                for (ParkingBlockDto pb : rootBlock.getBlocks()) {
                    if (pb.getId() == null) {
                        pb.setParentBlockId(rootBlock.getId());
                        pb.setParkingId(parkingId);
                        ParkingBlock blockInDb = parkingBlockRepo.save(new ParkingBlock(UUID.randomUUID().toString(), pb.getParkingId(), pb.getSequence(), pb.getName(), pb.getParentBlockId()));
                        pb.setId(blockInDb.getId());
                        updatedBlocks.add(new ParkingBlockDto(blockInDb.getId(), blockInDb.getParkingId(), blockInDb.getParentBlockId(), blockInDb.getBlockSequence(), blockInDb.getBlockName(), pb.getBlocks(), pb.getSpots()));
                    } else {
                        ParkingBlock blockInDb = parkingBlockRepo.findByIdAndUserId(pb.getId(), userServices.getUserId()).orElseThrow(() -> new ResourceNotFound("Block not found"));
                        blockInDb.setBlockName(pb.getName());
                        blockInDb.setBlockSequence(pb.getSequence());
                        parkingBlockRepo.save(blockInDb);
                        updatedBlocks.add(new ParkingBlockDto(blockInDb.getId(), blockInDb.getParkingId(), blockInDb.getParentBlockId(), blockInDb.getBlockSequence(), blockInDb.getBlockName(), pb.getBlocks(), pb.getSpots()));
                    }
                    presentInBlockDto.put(pb.getId(), true);
                    pb.setLocation(rootBlock.getLocation() + " " + pb.getName());
                    queue.add(pb);
                }

            }
        }
        List<ParkingBlock> parkingBlocksInParking = parkingBlockRepo.findByParkingId(parkingId).orElse(new ArrayList<>());
        List<ParkingBlock> toBeDeleteBlocks = new ArrayList<>();
        for (ParkingBlock block : parkingBlocksInParking) {
            if (!presentInBlockDto.containsKey(block.getId())) {
                toBeDeleteBlocks.add(block);
            }
        }
        parkingBlockRepo.deleteAll(toBeDeleteBlocks);
        for (int i = 0; i < numOfRootBlocks; i++) {
            responseBlocks.add(updatedBlocks.get(i));
        }
        return responseBlocks;
    }
}
