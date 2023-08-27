package com.amanboora.parking.services;

import com.amanboora.parking.dto.ParkingSpotDto;
import com.amanboora.parking.model.Parking;
import com.amanboora.parking.model.ParkingSpot;
import com.amanboora.parking.repository.ParkingRepo;
import com.amanboora.parking.repository.ParkingSpotRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class ParkingSpotServiceImplTest {

    private final Parking parking=new Parking();
    private final ParkingSpot parkingSpot=new ParkingSpot();
    private final String carNum="CH-02-AB-0007";

    @InjectMocks
    private ParkingSpotServiceImpl parkingSpotServiceImpl;
    @Mock
    private UserServices userServices;
    @Mock
    private ParkingRepo parkingRepo;
    @Mock
    private ParkingSpotRepo spotRepo;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        Mockito.when(userServices.getUserId()).thenReturn("user1");
        parking.setId("p1");
        parking.setParkingName("Parking1");
        parking.setParkingStatus("Active");
        parking.setUserId(userServices.getUserId());
        parkingSpot.setId("s1");
        parkingSpot.setSpotSequence(1L);
        parkingSpot.setSpotName("Spot 1");
        parkingSpot.setLocation("Spot 1");
        parkingSpot.setParkingId(parking.getId());
    }

    @Test
    public void shouldAllocateSpot(){
        Mockito.when(spotRepo.existsByCarNumberBySpotId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Mockito.when(spotRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parkingSpot));
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parking));
        ParkingSpotDto responseSpotDto= parkingSpotServiceImpl.spotAllocation("s1",carNum);
        Assertions.assertEquals(carNum,responseSpotDto.getCarNum());
    }

    @Test
    public void shouldNotAllocateSpotWhenSpotNotFound(){
        Mockito.when(spotRepo.existsByCarNumberBySpotId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Mockito.when(spotRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,()-> parkingSpotServiceImpl.spotAllocation("s1",carNum));
    }

    @Test
    public void shouldNotAllocateSpotWhenParkingNotFound(){
        Mockito.when(spotRepo.existsByCarNumberBySpotId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Mockito.when(spotRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parkingSpot));
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,()-> parkingSpotServiceImpl.spotAllocation("s1",carNum));
    }

    @Test
    public void shouldNotAllocateSpotWhenACarIsAlreadyParkedAtSpot(){
        parkingSpot.setCarNum("PARA-SF9");
        Mockito.when(spotRepo.existsByCarNumberBySpotId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Mockito.when(spotRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parkingSpot));
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parking));
        Assertions.assertThrows(RuntimeException.class,()-> parkingSpotServiceImpl.spotAllocation("s1",carNum));
    }

    @Test
    public void shouldNotAllocateSpotWhenACarIsAlreadyParkedInParking(){
        Mockito.when(spotRepo.existsByCarNumberBySpotId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        Assertions.assertThrows(RuntimeException.class,()-> parkingSpotServiceImpl.spotAllocation("s1",carNum));
    }

    @Test
    public void shouldNotAllocateSpotWhenParkingIsNotActivated(){
        parking.setParkingStatus("Inactive");
        Mockito.when(spotRepo.existsByCarNumberBySpotId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Mockito.when(spotRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parkingSpot));
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parking));
        Assertions.assertThrows(RuntimeException.class,()-> parkingSpotServiceImpl.spotAllocation("s1",carNum));
    }

    @Test
    public void shouldDeallocateSpot(){
        parkingSpot.setCarNum(carNum);
        Mockito.when(spotRepo.findByCarNumberAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parkingSpot));
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parking));
        Assertions.assertNull(parkingSpotServiceImpl.spotDeallocation(carNum).getCarNum());
    }

    @Test
    public void shouldNotDeallocateSpotWhenCarIsNotPresent(){
       Mockito.when(spotRepo.findByCarNumberAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.empty());
       Assertions.assertThrows(RuntimeException.class,()-> parkingSpotServiceImpl.spotDeallocation(carNum));
    }

    @Test
    public void shouldNotDeallocateSpotWhenParkingNotFound(){
        parkingSpot.setCarNum(carNum);
        Mockito.when(spotRepo.findByCarNumberAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parkingSpot));
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,()-> parkingSpotServiceImpl.spotDeallocation(carNum));
    }

    @Test
    public void shouldNotDeallocateSpotWhenParkingIsInDraftState(){
        parkingSpot.setCarNum(carNum);
        parking.setParkingStatus("Draft");
        Mockito.when(spotRepo.findByCarNumberAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parkingSpot));
        Mockito.when(parkingRepo.findByIdAndUserId(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(parking));
        Assertions.assertThrows(RuntimeException.class,()-> parkingSpotServiceImpl.spotDeallocation(carNum));
    }
}
