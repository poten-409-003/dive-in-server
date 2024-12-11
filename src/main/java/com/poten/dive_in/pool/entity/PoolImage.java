package com.poten.dive_in.pool.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class PoolImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imgId;

    @ManyToOne
    @JoinColumn(name = "pool_id" )
    private Pool pool;

    private String rprsImgYn; //대표이미지 여부
    private String imgUrl;

}

//public class PoolImage extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "pool_image_id")
//    private Long id;
//
//    private Boolean repImage; //대표이미지 여부
//
//    private String imageUrl;
//
//    @ManyToOne
//    @JoinColumn(name = "pool_id" )
//    private Pool pool;
//}
