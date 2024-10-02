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

    private String operatingHours;

    private String closingDays;

    private String latitude;

    private String longitude;

    private String contact;

    private Integer laneLength;

    private Integer laneCount;

    private String maxDepth;

    private String minDepth;

    private String facilities;

    private String region;

    public Pool toEntity(){
        return Pool.builder()
                .poolName(this.poolName)
                .poolAddress(this.poolAddress)
                .operatingHours(this.operatingHours)
                .closingDays(this.closingDays)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .contact(this.contact)
                .laneLength(this.laneLength)
                .laneCount(this.laneCount)
                .maxDepth(this.maxDepth)
                .minDepth(this.minDepth)
                .facilities(this.facilities)
                .region(this.region)
                .build();
    }
}
