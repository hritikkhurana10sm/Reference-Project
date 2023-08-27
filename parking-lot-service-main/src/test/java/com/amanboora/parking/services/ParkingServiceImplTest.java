package com.amanboora.parking.services;

import com.amanboora.parking.Exception.ResourceNotFound;
import com.amanboora.parking.dto.ParkingBlockDto;
import com.amanboora.parking.dto.ParkingDto;
import com.amanboora.parking.dto.ParkingSpotDto;
import com.amanboora.parking.model.Parking;
import com.amanboora.parking.model.ParkingBlock;
import com.amanboora.parking.model.ParkingSpot;
import com.amanboora.parking.repository.ParkingBlockRepo;
import com.amanboora.parking.repository.ParkingRepo;
import com.amanboora.parking.repository.ParkingSpotRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ParkingServiceImplTest {

    private final Parking parking = new Parking();
    private final ParkingBlockDto blockDto1 = new ParkingBlockDto();
    private final ParkingBlockDto blockDto2 = new ParkingBlockDto();
    private final ParkingSpotDto spotDto1 = new ParkingSpotDto();

    @InjectMocks
    private ParkingServiceImpl parkingServiceImpl;
    @InjectMocks
    private ParkingBlockServiceImpl blockServiceImpl;
    @InjectMocks
    private ParkingSpotServiceImpl spotServiceImpl;
    @Mock
    private UserServices userService;
    @Mock
    private ParkingRepo parkingRepo;
    @Mock
    private ParkingBlockRepo blockRepo;
    @Mock
    private ParkingSpotRepo spotRepo;

    @BeforeEach
    public void setup() throws Exception {
        blockServiceImpl = Mockito.spy(new ParkingBlockServiceImpl());
        MockitoAnnotations.openMocks(blockServiceImpl).close();
        spotServiceImpl = Mockito.spy(new ParkingSpotServiceImpl());
        MockitoAnnotations.openMocks(spotServiceImpl).close();
        MockitoAnnotations.openMocks(this).close();
        Mockito.when(userService.getUserId()).thenReturn("user1");
        parking.setId("1");
        parking.setParkingName("Parking 1");
        parking.setParkingStatus("Active");
        parking.setUserId("user1");
        blockDto1.setId("A");
        blockDto1.setName("Block A");
        blockDto1.setParkingId("1");
        blockDto1.setSequence(1L);
        blockDto2.setId("B");
        blockDto2.setName("Block B");
        blockDto2.setParkingId("1");
        blockDto2.setSequence(2L);
        spotDto1.setId("s1");
        spotDto1.setName("Spot 1");
        spotDto1.setParkingId("1");
        spotDto1.setParentBlockId(null);
        spotDto1.setSequence(1L);
        spotDto1.setLocation("");
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(parking));
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.initSynchronization();
        }
    }

    @Test
    public void testGetParkingWhenParkingExists() {
        spotDto1.setLocation(blockDto1.getName() + " " + blockDto2.getName());
        spotDto1.setParentBlockId(blockDto2.getId());
        blockDto2.setParentBlockId(blockDto1.getId());
        blockDto2.setSpots(new ArrayList<>(List.of(spotDto1)));
        blockDto1.setBlocks(new ArrayList<>(List.of(blockDto2)));
        List<ParkingBlock> blocksInParking = new ArrayList<>();
        blocksInParking.add(new ParkingBlock(blockDto1.getId(), blockDto1.getParkingId(), blockDto1.getSequence(), blockDto1.getName(), blockDto1.getParentBlockId()));
        blocksInParking.add(new ParkingBlock(blockDto2.getId(), blockDto2.getParkingId(), blockDto2.getSequence(), blockDto2.getName(), blockDto2.getParentBlockId()));
        List<ParkingSpot> spotsInParking = new ArrayList<>();
        spotsInParking.add(new ParkingSpot(spotDto1.getId(), spotDto1.getParkingId(), spotDto1.getParentBlockId(), spotDto1.getSequence(), spotDto1.getName(), spotDto1.getLocation()));
        Mockito.when(blockRepo.findByParkingId(Mockito.anyString())).thenReturn(Optional.of(blocksInParking));
        Mockito.when(spotRepo.findByParkingId(Mockito.anyString())).thenReturn(Optional.of(spotsInParking));
        ParkingDto parkingDto = parkingServiceImpl.getParking("1");
        Assertions.assertEquals("1", parkingDto.getId());
        Assertions.assertEquals("Parking 1", parkingDto.getName());
        Assertions.assertEquals("Active", parkingDto.getStatus());
        Assertions.assertEquals(1, parkingDto.getBlocks().size());
        Assertions.assertEquals("A", parkingDto.getBlocks().get(0).getId());
        Assertions.assertEquals("B", parkingDto.getBlocks().get(0).getBlocks().get(0).getId());
        Assertions.assertEquals(1, parkingDto.getBlocks().get(0).getBlocks().get(0).getSpots().size());
        Assertions.assertEquals("s1", parkingDto.getBlocks().get(0).getBlocks().get(0).getSpots().get(0).getId());
        Assertions.assertNull(parkingDto.getSpots());
        Mockito.verify(blockServiceImpl, Mockito.times(1)).getBlocksByParking(Mockito.anyString());
        Mockito.verify(spotServiceImpl, Mockito.times(1)).getSpotsByParking(Mockito.anyString());
    }

    @Test
    public void testGetParkingWhenParkingExistHavingOnlySpots() {
        List<ParkingSpot> spotsInParking = new ArrayList<>();
        spotsInParking.add(new ParkingSpot(spotDto1.getId(), spotDto1.getParkingId(), spotDto1.getParentBlockId(), spotDto1.getSequence(), spotDto1.getName(), spotDto1.getLocation()));
        Mockito.when(blockRepo.findByParkingId(Mockito.anyString())).thenReturn(Optional.of(new ArrayList<>()));
        Mockito.when(spotRepo.findByParkingId(Mockito.anyString())).thenReturn(Optional.of(spotsInParking));
        ParkingDto parkingDto = parkingServiceImpl.getParking("1");
        Assertions.assertEquals("1", parkingDto.getId());
        Assertions.assertEquals("Parking 1", parkingDto.getName());
        Assertions.assertEquals("Active", parkingDto.getStatus());
        Assertions.assertNull(parkingDto.getBlocks());
        Assertions.assertEquals(1, parkingDto.getSpots().size());
        Assertions.assertEquals("s1", parkingDto.getSpots().get(0).getId());
        Assertions.assertEquals("Spot 1", parkingDto.getSpots().get(0).getName());
        Mockito.verify(blockServiceImpl, Mockito.times(1)).getBlocksByParking(Mockito.anyString());
        Mockito.verify(spotServiceImpl, Mockito.times(1)).getSpotsByParking(Mockito.anyString());
    }

    @Test
    public void testGetParkingWhenParkingDoesNotExist() {
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFound.class, () -> parkingServiceImpl.getParking("1"));
        Mockito.verify(blockServiceImpl, Mockito.times(0)).getBlocksByParking(Mockito.anyString());
        Mockito.verify(spotServiceImpl, Mockito.times(0)).getSpotsByParking(Mockito.anyString());
    }

    @Test
    public void testGetParkingByUserWhenParkingExists() {
        List<Parking> parkings = new ArrayList<>();
        parkings.add(new Parking("1", userService.getUserId(), "Parking 1", "Active"));
        parkings.add(new Parking("2", userService.getUserId(), "Parking 2", "Active"));
        Mockito.when(parkingRepo.findByUserId(Mockito.anyString())).thenReturn(Optional.of(parkings));
        List<ParkingDto> parkingDtos = parkingServiceImpl.getParkingsByUser();
        Assertions.assertEquals("1", parkingDtos.get(0).getId());
        Assertions.assertEquals("Parking 1", parkingDtos.get(0).getName());
        Assertions.assertEquals("Active", parkingDtos.get(0).getStatus());
        Assertions.assertEquals("2", parkingDtos.get(1).getId());
        Assertions.assertEquals("Parking 2", parkingDtos.get(1).getName());
        Assertions.assertEquals("Active", parkingDtos.get(1).getStatus());
    }

    @Test
    public void testGetParkingByUserWhenParkingDoesNotExist() {
        Mockito.when(parkingRepo.findByUserId(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFound.class, parkingServiceImpl::getParkingsByUser);
    }

    @Test
    public void shouldRegisterParkingHavingBlocksAtLevel0() {
        spotDto1.setLocation(blockDto1.getName() + " " + blockDto2.getName());
        spotDto1.setParentBlockId(blockDto2.getId());
        blockDto2.setParentBlockId(blockDto1.getId());
        blockDto2.setSpots(new ArrayList<>(List.of(spotDto1)));
        blockDto1.setBlocks(new ArrayList<>(List.of(blockDto2)));
        List<ParkingBlockDto> blockDtos = new ArrayList<>();
        blockDtos.add(blockDto1);
        ParkingDto parkingDto = new ParkingDto("Parking 1", "Draft", blockDtos, null);
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenAnswer(invocation -> {
            Parking toBeSaveParking = invocation.getArgument(0);
            Assertions.assertEquals("Draft", toBeSaveParking.getParkingStatus());
            Assertions.assertEquals(userService.getUserId(), toBeSaveParking.getUserId());
            return invocation.getArgument(0);
        });
        Mockito.when(blockRepo.save(Mockito.any(ParkingBlock.class))).thenAnswer(invocation -> {
            ParkingBlock toBeSaveBlock = invocation.getArgument(0);
            if (toBeSaveBlock.getBlockSequence() == 1L) {
                Assertions.assertEquals("Block A", toBeSaveBlock.getBlockName());
                Assertions.assertNull(toBeSaveBlock.getParentBlockId());
            }
            if (toBeSaveBlock.getBlockSequence() == 2L)
                Assertions.assertEquals("Block B", toBeSaveBlock.getBlockName());
            return invocation.getArgument(0);
        });
        Mockito.when(spotRepo.save(Mockito.any(ParkingSpot.class))).thenAnswer(invocation -> {
            ParkingSpot toBeSaveSpot=invocation.getArgument(0);
            Assertions.assertEquals("Spot 1", toBeSaveSpot.getSpotName());
            Assertions.assertEquals(1L, toBeSaveSpot.getSpotSequence());
            return invocation.getArgument(0);
        });
        ParkingDto savedParkingDto = parkingServiceImpl.registerParking(parkingDto);
        Assertions.assertEquals("Parking 1", savedParkingDto.getName());
        Assertions.assertEquals("Draft", savedParkingDto.getStatus());
        Assertions.assertEquals(1, savedParkingDto.getBlocks().size());
        Assertions.assertEquals("Block A", savedParkingDto.getBlocks().get(0).getName());
        Assertions.assertEquals(1, savedParkingDto.getBlocks().get(0).getBlocks().size());
        Assertions.assertNull(savedParkingDto.getBlocks().get(0).getSpots());
        Assertions.assertEquals("Block B", savedParkingDto.getBlocks().get(0).getBlocks().get(0).getName());
        Assertions.assertEquals(1, savedParkingDto.getBlocks().get(0).getBlocks().get(0).getSpots().size());
        Assertions.assertNull(savedParkingDto.getBlocks().get(0).getBlocks().get(0).getBlocks());
        Assertions.assertEquals(0, savedParkingDto.getSpots().size());
        Mockito.verify(blockServiceImpl, Mockito.times(1)).addBlocks(Mockito.anyString(), Mockito.any(ParkingBlockDto.class));
        Mockito.verify(spotServiceImpl, Mockito.times(1)).addSpots(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void shouldRegisterParkingHavingSpotsAtLevel0() {
        List<ParkingSpotDto> spotDtos = new ArrayList<>();
        spotDtos.add(spotDto1);
        ParkingDto parkingDto = new ParkingDto("Parking 1", "Draft", null, spotDtos);
        parking.setParkingStatus("Draft");
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenReturn(parking);
        Mockito.when(spotRepo.save(Mockito.any(ParkingSpot.class))).thenReturn(new ParkingSpot(spotDto1.getId(), spotDto1.getParkingId(), spotDto1.getParentBlockId(), spotDto1.getSequence(), spotDto1.getName(), spotDto1.getLocation()));
        ParkingDto savedParkingDto = parkingServiceImpl.registerParking(parkingDto);
        Assertions.assertEquals("1", savedParkingDto.getId());
        Assertions.assertEquals("Parking 1", savedParkingDto.getName());
        Assertions.assertEquals("Draft", savedParkingDto.getStatus());
        Assertions.assertEquals(0, savedParkingDto.getBlocks().size());
        Assertions.assertEquals(1, savedParkingDto.getSpots().size());
        Mockito.verify(blockServiceImpl, Mockito.times(0)).addBlocks(Mockito.anyString(), Mockito.any());
        Mockito.verify(spotServiceImpl, Mockito.times(1)).addSpots(Mockito.anyList(), Mockito.anyString(), Mockito.any(), Mockito.anyString());
    }

    @Test
    public void shouldThrowExceptionWhenNoParkingNameInRegisterParkingDto() {
        ParkingDto parkingDto = new ParkingDto(null, "Draft", null, null);
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.registerParking(parkingDto));
    }

    @Test
    public void shouldThrowExceptionWhenRegisteredParkingNotInDraftStatus() {
        ParkingDto parkingDto = new ParkingDto();
        parkingDto.setId("1");
        parkingDto.setName("Parking 1");
        parkingDto.setStatus("Active");
        parking.setParkingStatus(parkingDto.getStatus());
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenReturn(parking);
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.registerParking(parkingDto));
    }

    @Test
    public void shouldThrowExceptionWhenRegisterParkingHavingBlocksAndSpotsAreAtSameLevel() {
        List<ParkingBlockDto> blockDtos = new ArrayList<>();
        blockDtos.add(blockDto1);
        blockDtos.add(blockDto2);
        List<ParkingSpotDto> spotDtos = new ArrayList<>();
        spotDtos.add(spotDto1);
        ParkingDto parkingDto = new ParkingDto("Parking 1", "Draft", blockDtos, spotDtos);
        parking.setParkingStatus("Draft");
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenReturn(parking);
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.registerParking(parkingDto));
        Mockito.verify(blockServiceImpl, Mockito.times(0)).addBlocks(Mockito.anyString(), Mockito.any());
        Mockito.verify(spotServiceImpl, Mockito.times(0)).addSpots(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void shouldThrowExceptionWhenRegisterParkingHavingSubBlocksAndSpotsAreAtSameLevel() {
        blockDto2.setParentBlockId(blockDto1.getId());
        spotDto1.setParentBlockId(blockDto1.getId());
        blockDto1.setSpots(new ArrayList<>(List.of(spotDto1)));
        blockDto1.setBlocks(new ArrayList<>(List.of(blockDto2)));
        List<ParkingBlockDto> blockDtos = new ArrayList<>();
        blockDtos.add(blockDto1);
        ParkingDto parkingDto = new ParkingDto("Parking 1", "Draft", blockDtos, null);
        parking.setParkingStatus("Draft");
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenReturn(parking);
        Mockito.when(blockRepo.save(Mockito.any(ParkingBlock.class))).thenReturn(new ParkingBlock(blockDto1.getId(), blockDto1.getParkingId(), blockDto1.getSequence(), blockDto1.getName(), blockDto1.getParentBlockId()));
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.registerParking(parkingDto));
        Mockito.verify(blockServiceImpl, Mockito.times(1)).addBlocks(Mockito.anyString(), Mockito.any(ParkingBlockDto.class));
        Mockito.verify(spotServiceImpl, Mockito.times(0)).addSpots(Mockito.anyList(), Mockito.anyString(), Mockito.any(), Mockito.anyString());
    }

    @Test
    public void shouldUpdateParking() {
        spotDto1.setLocation(blockDto1.getName() + " " + blockDto2.getName());
        spotDto1.setParentBlockId(blockDto2.getId());
        blockDto2.setParentBlockId(blockDto1.getId());
        blockDto2.setSpots(new ArrayList<>(List.of(spotDto1)));
        ParkingBlockDto deletingBlockDto = new ParkingBlockDto("D", parking.getId(), blockDto1.getId(), 4L, "Block D");
        ParkingSpotDto deletingSpotDto = new ParkingSpotDto("s3", parking.getId(), deletingBlockDto.getId(), "Spot 3", 3L, "Block A Block D Spot 3");
        deletingBlockDto.setSpots(List.of(deletingSpotDto));
        ParkingSpotDto newSpotDto = new ParkingSpotDto();
        newSpotDto.setName("Spot 2");
        newSpotDto.setSequence(2L);
        ParkingBlockDto newBlockDto = new ParkingBlockDto();
        newBlockDto.setName("Block C");
        newBlockDto.setSequence(3L);
        newBlockDto.setSpots(List.of(newSpotDto));
        blockDto1.setBlocks(new ArrayList<>(Arrays.asList(blockDto2, newBlockDto)));
        List<ParkingBlockDto> blockDtos = new ArrayList<>();
        blockDtos.add(blockDto1);
        List<ParkingBlock> blocksInDb = new ArrayList<>();
        blocksInDb.add(new ParkingBlock(blockDtos.get(0).getId(), blockDtos.get(0).getParkingId(), blockDtos.get(0).getSequence(), blockDtos.get(0).getName(), blockDtos.get(0).getParentBlockId()));
        blocksInDb.add(new ParkingBlock(blockDtos.get(0).getBlocks().get(0).getId(), blockDtos.get(0).getBlocks().get(0).getParkingId(), blockDtos.get(0).getBlocks().get(0).getSequence(), blockDtos.get(0).getBlocks().get(0).getName(), blockDtos.get(0).getBlocks().get(0).getParentBlockId()));
        blocksInDb.add(new ParkingBlock("C", blockDtos.get(0).getParkingId(), blockDtos.get(0).getBlocks().get(1).getSequence(), blockDtos.get(0).getBlocks().get(1).getName(), blockDtos.get(0).getId()));
        blocksInDb.add(new ParkingBlock(deletingBlockDto.getId(), deletingBlockDto.getParkingId(), deletingBlockDto.getSequence(), deletingBlockDto.getName(), deletingBlockDto.getParentBlockId()));
        List<ParkingSpot> spotsInDb = new ArrayList<>();
        spotsInDb.add(new ParkingSpot(spotDto1.getId(), spotDto1.getParkingId(), spotDto1.getParentBlockId(), spotDto1.getSequence(), spotDto1.getName(), spotDto1.getLocation()));
        spotsInDb.add(new ParkingSpot("s2", spotDto1.getParkingId(), "C", newSpotDto.getSequence(), newSpotDto.getName(), "Block A Block C Spot 2"));
        spotsInDb.add(new ParkingSpot(deletingSpotDto.getId(), deletingSpotDto.getParkingId(), deletingSpotDto.getParentBlockId(), deletingSpotDto.getSequence(), deletingSpotDto.getName(), deletingSpotDto.getLocation()));
        ParkingDto parkingDto = new ParkingDto("Parking 2", "Inactive", blockDtos, null);
        parking.setParkingName("Parking 2");
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenReturn(parking);
        Mockito.when(blockRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(blocksInDb.get(0))).thenReturn(Optional.of(blocksInDb.get(1)));
        Mockito.when(blockRepo.findByParkingId(Mockito.anyString())).thenReturn(Optional.of(blocksInDb));
        Mockito.when(blockRepo.save(Mockito.any(ParkingBlock.class))).thenReturn(blocksInDb.get(0)).thenReturn(blocksInDb.get(1)).thenReturn(blocksInDb.get(2));
        Mockito.when(spotRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(spotsInDb.get(0)));
        Mockito.when(spotRepo.findByParentBlockId(Mockito.anyString())).thenReturn(Optional.of(spotsInDb));
        Mockito.when(spotRepo.save(Mockito.any(ParkingSpot.class))).thenReturn(spotsInDb.get(0)).thenReturn(spotsInDb.get(1));
        ParkingDto updatedParkingDto = parkingServiceImpl.updateParking("1", parkingDto);
        Assertions.assertEquals("1", updatedParkingDto.getId());
        Assertions.assertEquals("Parking 2", updatedParkingDto.getName());
        Assertions.assertEquals("Inactive", updatedParkingDto.getStatus());
        Assertions.assertEquals(1, updatedParkingDto.getBlocks().size());
        Assertions.assertEquals("Block A", updatedParkingDto.getBlocks().get(0).getName());
        Assertions.assertEquals("Block B", updatedParkingDto.getBlocks().get(0).getBlocks().get(0).getName());
        Assertions.assertEquals("s1", updatedParkingDto.getBlocks().get(0).getBlocks().get(0).getSpots().get(0).getId());
        Assertions.assertEquals("Block C", updatedParkingDto.getBlocks().get(0).getBlocks().get(1).getName());
        Assertions.assertNull(updatedParkingDto.getBlocks().get(0).getBlocks().get(1).getBlocks());
        Assertions.assertEquals(1, updatedParkingDto.getBlocks().get(0).getBlocks().get(1).getSpots().size());
        Assertions.assertEquals("s2", updatedParkingDto.getBlocks().get(0).getBlocks().get(1).getSpots().get(0).getId());
        Assertions.assertEquals("Block A Block C Spot 2", updatedParkingDto.getBlocks().get(0).getBlocks().get(1).getSpots().get(0).getLocation());
        Assertions.assertEquals(0, updatedParkingDto.getSpots().size());
        Mockito.verify(blockServiceImpl, Mockito.times(1)).updateBlocks(Mockito.anyString(), Mockito.any());
        Mockito.verify(spotServiceImpl, Mockito.times(2)).updateSpots(Mockito.anyString(), Mockito.any(), Mockito.anyList(), Mockito.anyString());
    }

    @Test
    public void shouldNotUpdateParkingWhenParkingStatusIsActive() {
        ParkingDto parkingDto = new ParkingDto("Parking 2", "Active", null, null);
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.updateParking("1", parkingDto));
    }

    @Test
    public void shouldNotUpdateParkingWhenNoParkingFoundForUser() {
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
        ParkingDto parkingDto = new ParkingDto("Parking 2", "Draft", null, null);
        Assertions.assertThrows(ResourceNotFound.class, () -> parkingServiceImpl.updateParking("1", parkingDto));
    }

    @Test
    public void shouldNotUpdateParkingWhenNoBlockFoundForUser() {
        List<ParkingBlockDto> blockDtos = new ArrayList<>();
        List<ParkingBlockDto> subBlockDtos = new ArrayList<>();
        blockDto2.setParentBlockId(blockDto1.getId());
        subBlockDtos.add(blockDto2);
        blockDto1.setBlocks(subBlockDtos);
        blockDtos.add(blockDto1);
        ParkingDto parkingDto = new ParkingDto("Parking 2", "Inactive", blockDtos, null);
        parking.setParkingName("Parking 2");
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenReturn(parking);
        Mockito.when(blockRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFound.class, () -> parkingServiceImpl.updateParking("1", parkingDto));
        Mockito.verify(blockServiceImpl, Mockito.times(1)).updateBlocks(Mockito.anyString(), Mockito.any());
        Mockito.verify(spotServiceImpl, Mockito.times(0)).updateSpots(Mockito.anyString(), Mockito.any(), Mockito.anyList(), Mockito.anyString());
    }

    @Test
    public void shouldNotUpdateParkingWhenNoSpotFoundForUser() {
        List<ParkingSpotDto> spotDtos = new ArrayList<>();
        spotDtos.add(spotDto1);
        ParkingDto parkingDto = new ParkingDto("Parking 2", "Inactive", null, spotDtos);
        parking.setParkingName("Parking 2");
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenReturn(parking);
        Mockito.when(spotRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFound.class, () -> parkingServiceImpl.updateParking("1", parkingDto));
        Mockito.verify(blockServiceImpl, Mockito.times(0)).updateBlocks(Mockito.anyString(), Mockito.any());
        Mockito.verify(spotServiceImpl, Mockito.times(1)).updateSpots(Mockito.anyString(), Mockito.any(), Mockito.anyList(), Mockito.anyString());
    }

    @Test
    public void shouldNotUpdateParkingWhenBlocksAndSpotsAreAtSameLevelInDto() {
        List<ParkingBlockDto> blockDtos = new ArrayList<>();
        blockDtos.add(blockDto1);
        blockDtos.add(blockDto2);
        List<ParkingSpotDto> spotDtos = new ArrayList<>();
        spotDtos.add(spotDto1);
        ParkingDto parkingDto = new ParkingDto("Parking 1", "Draft", blockDtos, spotDtos);
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.updateParking("1", parkingDto));
        Mockito.verify(blockServiceImpl, Mockito.times(0)).addBlocks(Mockito.anyString(), Mockito.any());
        Mockito.verify(spotServiceImpl, Mockito.times(0)).addSpots(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void shouldNotUpdateParkingWhenSubBlocksAndSpotsAreAtSameLevelInDto() {
        blockDto2.setParentBlockId(blockDto1.getId());
        spotDto1.setParentBlockId(blockDto1.getId());
        blockDto1.setSpots(new ArrayList<>(List.of(spotDto1)));
        blockDto1.setBlocks(new ArrayList<>(List.of(blockDto2)));
        List<ParkingBlockDto> blockDtos = new ArrayList<>();
        blockDtos.add(blockDto1);
        ParkingDto parkingDto = new ParkingDto("Parking 1", "Draft", blockDtos, null);
        parking.setParkingStatus("Draft");
        Mockito.when(parkingRepo.save(Mockito.any(Parking.class))).thenReturn(parking);
        Mockito.when(blockRepo.save(Mockito.any(ParkingBlock.class))).thenReturn(new ParkingBlock(blockDto1.getId(), blockDto1.getParkingId(), blockDto1.getSequence(), blockDto1.getName(), blockDto1.getParentBlockId()));
        Mockito.when(blockRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new ParkingBlock(blockDto1.getId(), blockDto1.getParkingId(), blockDto1.getSequence(), blockDto1.getName(), blockDto1.getParentBlockId())));
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.updateParking("1", parkingDto));
        Mockito.verify(blockServiceImpl, Mockito.times(1)).updateBlocks(Mockito.anyString(), Mockito.any());
        Mockito.verify(spotServiceImpl, Mockito.times(0)).updateSpots(Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyString());
    }

    @Test
    public void shouldUpdateParkingStatus() {
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(parking));
        Assertions.assertEquals("Inactive", parkingServiceImpl.updateParkingStatus("1", "Inactive").getStatus());
    }

    @Test
    public void shouldNotUpdateParkingStatusWhenNoParkingFoundForUser() {
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFound.class, () -> parkingServiceImpl.updateParkingStatus("1", "Active"));
    }

    @Test
    public void shouldNotUpdateParkingStatusWhenNewStatusIsDraft() {
        parking.setParkingStatus("Inactive");
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(parking));
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.updateParkingStatus("1", "Draft"));
    }

    @Test
    public void shouldNotUpdateParkingStatusWhenNewAndOldStatusAreActive() {
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(parking));
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.updateParkingStatus("1", "Active"));
    }

    @Test
    public void shouldNotUpdateParkingStatusWhenNewAndOldStatusAreInactive() {
        parking.setParkingStatus("Inactive");
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(parking));
        Assertions.assertThrows(RuntimeException.class, () -> parkingServiceImpl.updateParkingStatus("1", "Inactive"));
    }
}
