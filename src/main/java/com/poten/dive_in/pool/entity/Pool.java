package com.poten.dive_in.pool.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.pool.dto.PoolRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter @Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Pool extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoolImage> imageList;


    public void addImage(List<PoolImage> poolImageList) {
        this.imageList = poolImageList;
    }

    public void updatePool(PoolRequestDto poolRequestDto) {
        this.poolName = poolRequestDto.getPoolName();
        this.poolAddress = poolRequestDto.getPoolAddress();
    }

    public void replaceImageList(List<PoolImage> newPoolImageList){
        if (this.imageList != null) {
            this.imageList.clear();
            this.imageList.addAll(newPoolImageList);
        } else{
            this.imageList = new ArrayList<>(newPoolImageList);
        }
    }

}