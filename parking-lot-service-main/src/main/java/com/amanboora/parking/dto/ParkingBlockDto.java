package com.amanboora.parking.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ParkingBlockDto implements Serializable {
    private String id;
    private String parkingId;
    private String parentBlockId;
    private Long sequence;
    private String name;
    private List<ParkingBlockDto> blocks;
    private List<ParkingSpotDto> spots;
    private String location;

    public ParkingBlockDto() {
    }

    public ParkingBlockDto(String id, String parkingId, String parentBlockId, Long sequence, String name) {
        this.id = id;
        this.parkingId = parkingId;
        this.parentBlockId = parentBlockId;
        this.sequence = sequence;
        this.name = name;
    }

    public ParkingBlockDto(String id, String parkingId, String parentBlockId, Long sequence, String name,
                           List<ParkingBlockDto> blocks, List<ParkingSpotDto> spots) {
        this.id = id;
        this.parkingId = parkingId;
        this.parentBlockId = parentBlockId;
        this.sequence = sequence;
        this.name = name;
        this.blocks = blocks;
        this.spots = spots;
    }
}
