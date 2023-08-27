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
@Table(name="parking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parking {
    @Id
    private String id;

    @Column(name="user_id")
    private String userId;

    @Column(name="name")
    private String parkingName;

    @Column(name="status")
    private String parkingStatus;
}
