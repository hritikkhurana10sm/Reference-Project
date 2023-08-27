package com.amanboora.parking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="parking_block")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingBlock {

    @Id
    private String id;

    @Column(name="parking_id")
    private String parkingId;

    @Column(name="sequence")
    private Long blockSequence;

    @Column(name="name")
    private String blockName;

    @Column(name="parent_block_id")
    private String parentBlockId;

}
