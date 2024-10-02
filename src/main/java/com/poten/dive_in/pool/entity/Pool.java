package com.poten.dive_in.pool.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.lesson.entity.Lesson;
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

    private String closingDays;

    private String latitude;

    private String longitude;

    private String contact;

    private Integer laneLength;

    private Integer laneCount;

    private Float maxDepth;

    private Float minDepth;

    private String facilities;

    private String region;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoolImage> imageList;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessonList;

    public void addLesson(Lesson lesson) {
        if (!lessonList.contains(lesson)) {
            lessonList.add(lesson);
        }    }

    public void addImage(List<PoolImage> poolImageList) {
        this.imageList = poolImageList;
    }

    public void updatePool(PoolRequestDto poolRequestDto) {
        this.poolName = poolRequestDto.getPoolName();
        this.poolAddress = poolRequestDto.getPoolAddress();
        this.operatingHours = poolRequestDto.getOperatingHours();
        this.closingDays = poolRequestDto.getClosingDays();
        this.latitude = poolRequestDto.getLatitude();
        this.longitude = poolRequestDto.getLongitude();
        this.contact = poolRequestDto.getContact();
        this.laneLength = poolRequestDto.getLaneLength();
        this.laneCount = poolRequestDto.getLaneCount();
        this.maxDepth = poolRequestDto.getMaxDepth();
        this.minDepth = poolRequestDto.getMinDepth();
        this.facilities = poolRequestDto.getFacilities();
        this.region = poolRequestDto.getRegion();
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