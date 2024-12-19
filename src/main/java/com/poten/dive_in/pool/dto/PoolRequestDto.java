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
                .poolName(this.poolName)
                .roadAddress(this.poolAddress)
                .operatingHours(this.operatingHours)
                .dayOff(this.closingDays)
                .latitude(Double.valueOf(this.latitude) )
                .longitude(Double.valueOf(this.longitude))
                .telephone(this.contact)
                .laneLength(String.valueOf(this.laneLength))
                .laneCount(String.valueOf(this.laneCount))
                .maximumDepth(this.maxDepth)
                .minimumDepth(this.minDepth)
//                .region(this.region)
                .viewCount(this.viewCnt)
                .build();
    }
}
