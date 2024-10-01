package com.poten.dive_in.entity;

import jakarta.persistence.*;

@Entity
public class PoolImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pool_image_id")
    private Long id;

    private Boolean repImage;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "pool_id" )
    private Pool pool;
}
