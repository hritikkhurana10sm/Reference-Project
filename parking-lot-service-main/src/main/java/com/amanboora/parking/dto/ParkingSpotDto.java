package com.amanboora.parking.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ParkingSpotDto implements Serializable {
    private String id;
    private String parkingId;
    private String parentBlockId;
    private String name;
    private Long sequence;
    private String location;
    private String carNum;

    public ParkingSpotDto() {
    }

    public ParkingSpotDto(String id, String parkingId, String parentBlockId, String name, Long sequence, String location) {
        this.id = id;
        this.parkingId = parkingId;
        this.parentBlockId = parentBlockId;
        this.name = name;
        this.sequence = sequence;
        this.location = location;
    }

    public ParkingSpotDto(String id, String name, Long sequence, String location, String carNum) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.location = location;
        this.carNum = carNum;
    }

    public ParkingSpotDto(String id, String parkingId, String parentBlockId, String name, Long sequence, String location, String carNum) {
        this.id = id;
        this.parkingId = parkingId;
        this.parentBlockId = parentBlockId;
        this.name = name;
        this.sequence = sequence;
        this.location = location;
        this.carNum = carNum;
    }
}
