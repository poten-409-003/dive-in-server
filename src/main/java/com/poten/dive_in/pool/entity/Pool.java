package com.poten.dive_in.pool.entity;

import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.lesson.entity.SwimClass;
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
public class Pool extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pool_id") // 풀 ID
    private Long poolId;

    @Column(name = "pool_nm") // 수영장 이름
    private String poolName;

    @Column(name = "rd_nm_addr") // 도로명 주소
    private String roadAddress;

    @Column(name = "zip_cd") // 우편번호
    private String zipCode;

    @Column(name = "oprt_hr") // 운영 시간
    private String operatingHours;

    @Column(name = "dy_of_day") // 휴무일
    private String dayOff;

    @Column(name = "lttd") // 위도
    private Double latitude;

    @Column(name = "hrdn") // 경도
    private Double longitude;

    @Column(name = "telno") // 연락처
    private String telephone;

    @Column(name = "lane_lngt") // 레인 길이
    private String laneLength;

    @Column(name = "lane_cnt") // 레인 수
    private String laneCount;

    @Column(name = "min_dpt") // 최소 수심
    private Float minimumDepth;

    @Column(name = "max_dpt") // 최대 수심
    private Float maximumDepth;

    @JoinColumn(name = "amnt_cd", referencedColumnName = "cd") // 편의시설 코드
    @ManyToOne
    private CommonCode amenityCode;

    @Column(name = "chc_cnt") // 조회수
    private Integer viewCount;

    @Column(name = "use_yn") // 사용 여부
    private String isActive;


    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoolImage> imageList;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SwimClass> swimClassList;

    public void addImage(List<PoolImage> poolImageList) {
        this.imageList = poolImageList;
    }

    public void addLesson(SwimClass swimClass) {
        if (!swimClassList.contains(swimClass)) {
            swimClassList.add(swimClass);
        }
    }

    public void updatePool(PoolRequestDto poolRequestDto) {
        this.poolName = poolRequestDto.getPoolName();
        this.roadAddress = poolRequestDto.getPoolAddress();
        this.zipCode = poolRequestDto.getZipCode();
        this.operatingHours = poolRequestDto.getOperatingHours();
        this.dayOff = poolRequestDto.getClosingDays();
        this.latitude = Double.valueOf(poolRequestDto.getLatitude());
        this.longitude = Double.valueOf(poolRequestDto.getLongitude());
        this.telephone = poolRequestDto.getContact();
        this.laneLength = String.valueOf(poolRequestDto.getLaneLength());
        this.laneCount = String.valueOf(poolRequestDto.getLaneCount());
        this.maximumDepth = poolRequestDto.getMaxDepth();
        this.minimumDepth = poolRequestDto.getMinDepth();
//        this.amenityCode = poolRequestDto.getFacilities();
        this.viewCount = poolRequestDto.getViewCnt();
//        this.region = poolRequestDto.getRegion();
    }

    public void replaceImageList(List<PoolImage> newPoolImageList) {
        if (this.imageList != null) {
            this.imageList.clear();
            this.imageList.addAll(newPoolImageList);
        } else {
            this.imageList = new ArrayList<>(newPoolImageList);
        }
    }


    public void updateAmenityCode(CommonCode commonCode) {
        this.amenityCode = commonCode;
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