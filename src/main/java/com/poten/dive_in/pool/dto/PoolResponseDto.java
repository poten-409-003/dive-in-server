package com.poten.dive_in.pool.dto;

import com.poten.dive_in.pool.entity.Pool;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Builder
public class PoolResponseDto {

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

    private List<PoolImageDto> poolImageDtoList;

    public static PoolResponseDto ofEntity(Pool pool){

        List<PoolImageDto> poolImageDtoList = (pool.getImageList() != null) ?
                pool.getImageList().stream()
                        .map(PoolImageDto::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        return PoolResponseDto.builder()
                .id(pool.getId())
                .poolName(pool.getPoolName())
                .poolAddress(pool.getPoolAddress())
                .operatingHours(pool.getOperatingHours())
                .latitude(pool.getLatitude())
                .longitude(pool.getLongitude())
                .contact(pool.getContact())
                .laneLength(pool.getLaneLength())
                .laneCount(pool.getLaneCount())
                .poolDepth(pool.getPoolDepth())
                .facilities(pool.getFacilities())
                .region(pool.getRegion())
                .poolImageDtoList(poolImageDtoList)
                .build();
        }
    }

