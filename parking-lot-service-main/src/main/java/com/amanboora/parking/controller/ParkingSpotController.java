package com.amanboora.parking.controller;

import com.amanboora.parking.Exception.ResourceNotFound;
import com.amanboora.parking.dao.ParkingService;
import com.amanboora.parking.dao.ParkingSpotService;
import com.amanboora.parking.dto.ParkingSpotDto;
import com.amanboora.parking.generics.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;
    private final ParkingService parkingService;

    @Autowired
    public ParkingSpotController(ParkingSpotService parkingSpotService,ParkingService parkingService) {
        this.parkingSpotService = parkingSpotService;
        this.parkingService=parkingService;
    }

    @PutMapping("/spot/{id}/car-enter")
    public ResponseEntity<Response<ParkingSpotDto>> spotAllocation(@PathVariable("id") String id, @RequestBody String carNum) {
        ParkingSpotDto parkingSpotDto = parkingSpotService.spotAllocation(id, carNum);
        List<String> responseMessage = new ArrayList<>();
        responseMessage.add("Spot is successfully allotted to " + carNum);
        Response<ParkingSpotDto> response = new Response<>(parkingSpotDto, responseMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{parkingId}/spot/car-enter")
    public ResponseEntity<Response<ParkingSpotDto>> automatedSpotAllocation(@PathVariable("parkingId") String parkingId, @RequestBody String carNum) {
        if(!parkingService.existByParkingIdAndUserId(parkingId)){
            throw new ResourceNotFound("Parking Not Found");
        }
        ParkingSpotDto parkingSpotDto = parkingSpotService.automatedSpotAllocation(parkingId, carNum);
        List<String> responseMessage = new ArrayList<>();
        responseMessage.add("Spot is successfully allotted to " + carNum);
        Response<ParkingSpotDto> response = new Response<>(parkingSpotDto, responseMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/spot/car-exit")
    public ResponseEntity<Response<ParkingSpotDto>> spotDeallocation(@RequestBody String carNum) {
        ParkingSpotDto parkingSpotDto = parkingSpotService.spotDeallocation(carNum);
        List<String> responseMessage = new ArrayList<>();
        responseMessage.add(carNum + " exits successfully from the parking!");
        Response<ParkingSpotDto> response = new Response<>(parkingSpotDto, responseMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
