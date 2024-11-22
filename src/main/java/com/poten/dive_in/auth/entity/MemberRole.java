package com.poten.dive_in.auth.entity;

import jakarta.persistence.*;

@Entity
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_nm")
    private String name;

    @Column(name = "role_expln")
    private String explain;

    private Character useYn;
}
