package com.poten.dive_in.entity;

import jakarta.persistence.*;

@Entity
public class Academy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academy_id")
    private Long id;

    private String academyName;

    private String academyInfo;

    private String profileImageUrl;

}
