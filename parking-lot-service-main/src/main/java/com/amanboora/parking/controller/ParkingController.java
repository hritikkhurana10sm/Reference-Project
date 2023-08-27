package com.amanboora.parking.controller;

import com.amanboora.parking.dao.ParkingService;
import com.amanboora.parking.dto.ParkingDto;
import com.amanboora.parking.generics.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ParkingController {

    private final ParkingService parkingService;

    @Autowired
    public ParkingController(ParkingService parkingService){

        this.parkingService = parkingService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response<List<ParkingDto>>> getParkingsByUser() {
        List<ParkingDto> listParkingDto = parkingService.getParkingsByUser();
        Response<List<ParkingDto>> response = new Response<>(listParkingDto, new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ParkingDto>> getParking(@PathVariable("id") String id) {
        ParkingDto parkingDto = parkingService.getParking(id);
        Response<ParkingDto> response = new Response<>(parkingDto, new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Response<ParkingDto>> registerParking(@RequestBody ParkingDto parking) {
        List<String> messages = new ArrayList<>();
        ParkingDto parkingDto = parkingService.registerParking(parking);
        messages.add("Saved!");
        messages.add("Parking had been registered.");
        Response<ParkingDto> response = new Response<>(parkingDto, messages);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response<ParkingDto>> updateParking(@PathVariable("id") String id, @RequestBody ParkingDto parking) {
        ParkingDto parkingDto = parkingService.updateParking(id, parking);
        List<String> messages = new ArrayList<>();
        messages.add("Updated!");
        messages.add("Parking has been updated.");
        Response<ParkingDto> response = new Response<>(parkingDto, messages);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}/status")
    public ResponseEntity<Response<ParkingDto>> updateParkingStatus(@PathVariable("id") String id, @RequestBody String status){
        ParkingDto parkingDto= parkingService.updateParkingStatus(id, status);
        List<String> messages = new ArrayList<>();
        messages.add("Updated!");
        messages.add("Parking Status has been updated.");
        Response<ParkingDto> response = new Response<>(parkingDto, messages);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
