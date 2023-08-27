package com.amanboora.parking.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ParkingDto implements Serializable {

    private String id;
    private String name;
    private String status;
    private List<ParkingBlockDto> blocks;
    private List<ParkingSpotDto> spots;

    public ParkingDto() {
    }

    public ParkingDto(String name, String status, List<ParkingBlockDto> blocks, List<ParkingSpotDto> spots) {
        this.name = name;
        this.status = status;
        this.blocks = blocks;
        this.spots = spots;
    }

    public ParkingDto(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public ParkingDto(String id, String name, String status, List<ParkingBlockDto> blocks, List<ParkingSpotDto> spots) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.blocks = blocks;
        this.spots = spots;
    }
}
