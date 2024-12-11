package com.poten.dive_in.pool.dto;

import com.poten.dive_in.pool.entity.Pool;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PoolRequestDto {

    @NotBlank(message = "수영장 이름은 필수입니다.")
    private String poolName;

    private String poolAddress;

    private String zipCode;
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

    private Integer viewCnt;

    private String region;

    public Pool toEntity(){
        return Pool.builder()
                .poolNm(this.poolName)
                .rdNmAddr(this.poolAddress)
                .oprtHr(this.operatingHours)
                .dyOfDay(this.closingDays)
                .lttd(Double.valueOf(this.latitude) )
                .hrdn(Double.valueOf(this.longitude))
                .telno(this.contact)
                .laneLngt(String.valueOf(this.laneLength))
                .laneCnt(String.valueOf(this.laneCount))
                .maxDpt(this.maxDepth)
                .minDpt(this.minDepth)
                .amntCd(this.facilities)
//                .region(this.region)
                .chcCnt(this.viewCnt)
                .build();
    }
}
