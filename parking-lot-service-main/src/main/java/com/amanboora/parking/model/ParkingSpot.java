package com.amanboora.parking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parking_spot", indexes = @Index(name="car_num_index", columnList = "car_number"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpot {

    @Id
    private String id;

    @Version
    @Column(name="version")
    private int version;

    @Column(name = "parking_id")
    private String parkingId;

    @Column(name = "parent_block_id")
    private String parentBlockId;

    @Column(name = "sequence")
    private Long spotSequence;

    @Column(name = "name")
    private String spotName;

    @Column(name = "location")
    private String location;

    @Column(name = "car_number")
    private String carNum;

    public ParkingSpot(String id, String parkingId, String parentBlockId, Long spotSequence, String spotName, String location) {
        this.id = id;
        this.parkingId = parkingId;
        this.parentBlockId = parentBlockId;
        this.spotSequence = spotSequence;
        this.spotName = spotName;
        this.location = location;
    }
}
