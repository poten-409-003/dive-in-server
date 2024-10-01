package com.poten.dive_in.entity;

import jakarta.persistence.*;

@Entity
public class Pool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pool_id")
    private Long id;

    private String poolName;

    private String poolAddress;

    private String operatingHours;

    private String latitude;

    private String longitude;

    private String contact;

    private Integer laneLength;

    private Integer laneCount;

    private String poolDepth;

    private String facilities;

    private String region;
}
