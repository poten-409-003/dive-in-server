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

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pool")
public class Pool  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poolId;

    private String poolNm;
    private String rdNmAddr; //도로명주소
    private String zipCd; //우편번호
    private String oprtHr; //운영시간
    private String dyOfDay; //휴무일
    private Double lttd; //위도
    private Double hrdn; //경도
    private String telno; //연락처
    private String laneLngt; //레인 길이
    private String laneCnt; //레인 수
    private Float minDpt; //최소수심
    private Float maxDpt; //최대수심
    private String amntCd; //편의시설 코드
    private Integer chcCnt; //조회수
    private String useYn; //사용여부

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoolImage> imageList;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessonList;

    public void addImage(List<PoolImage> poolImageList) {
        this.imageList = poolImageList;
    }

    public void addLesson(Lesson lesson) {
        if (!lessonList.contains(lesson)) {
            lessonList.add(lesson);
        }
    }

    public void updatePool(PoolRequestDto poolRequestDto) {
        this.poolNm = poolRequestDto.getPoolName();
        this.rdNmAddr = poolRequestDto.getPoolAddress();
        this.zipCd = poolRequestDto.getZipCode();
        this.oprtHr = poolRequestDto.getOperatingHours();
        this.dyOfDay = poolRequestDto.getClosingDays();
        this.lttd = Double.valueOf(poolRequestDto.getLatitude()) ;
        this.hrdn = Double.valueOf(poolRequestDto.getLongitude());
        this.telno = poolRequestDto.getContact();
        this.laneLngt = String.valueOf(poolRequestDto.getLaneLength()) ;
        this.laneCnt =  String.valueOf(poolRequestDto.getLaneCount());
        this.maxDpt = poolRequestDto.getMaxDepth();
        this.minDpt = poolRequestDto.getMinDepth();
        this.amntCd = poolRequestDto.getFacilities();
        this.chcCnt = poolRequestDto.getViewCnt();
//        this.region = poolRequestDto.getRegion();
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

//
//import com.poten.dive_in.common.entity.BaseTimeEntity;
//import com.poten.dive_in.lesson.entity.Lesson;
//import com.poten.dive_in.pool.dto.PoolRequestDto;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.thymeleaf.standard.expression.Each;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter @Builder
//@RequiredArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Pool extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "pool_id")
//    private Long id;
//
//    private String poolName;
//
//    private String poolAddress;
//
//    private String operatingHours;
//
//    private String closingDays;
//
//    private String latitude;
//
//    private String longitude;
//
//    private String contact;
//
//    private Integer laneLength;
//
//    private Integer laneCount;
//
//    private Float maxDepth;
//
//    private Float minDepth;
//
//    private String facilities;
//
//    private String region;
//
//    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PoolImage> imageList;
//
//    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Lesson> lessonList;
//
//    public void addLesson(Lesson lesson) {
//        if (!lessonList.contains(lesson)) {
//            lessonList.add(lesson);
//        }    }
//
//    public void addImage(List<PoolImage> poolImageList) {
//        this.imageList = poolImageList;
//    }
//
//    public void updatePool(PoolRequestDto poolRequestDto) {
//        this.poolName = poolRequestDto.getPoolName();
//        this.poolAddress = poolRequestDto.getPoolAddress();
//        this.operatingHours = poolRequestDto.getOperatingHours();
//        this.closingDays = poolRequestDto.getClosingDays();
//        this.latitude = poolRequestDto.getLatitude();
//        this.longitude = poolRequestDto.getLongitude();
//        this.contact = poolRequestDto.getContact();
//        this.laneLength = poolRequestDto.getLaneLength();
//        this.laneCount = poolRequestDto.getLaneCount();
//        this.maxDepth = poolRequestDto.getMaxDepth();
//        this.minDepth = poolRequestDto.getMinDepth();
//        this.facilities = poolRequestDto.getFacilities();
//        this.region = poolRequestDto.getRegion();
//    }
//
//    public void replaceImageList(List<PoolImage> newPoolImageList){
//        if (this.imageList != null) {
//            this.imageList.clear();
//            this.imageList.addAll(newPoolImageList);
//        } else{
//            this.imageList = new ArrayList<>(newPoolImageList);
//        }
//    }
//
//}