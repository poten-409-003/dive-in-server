package com.poten.dive_in.pool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poten.dive_in.lesson.dto.LessonListResponseDto;
import com.poten.dive_in.pool.entity.Pool;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Builder
public class PoolDetailResponseDto {

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

    @JsonProperty("pool_image")
    private List<PoolImageDto> poolImageDtoList;

    @JsonProperty("lesson")
    private List<LessonListResponseDto> lessonListResponseDtoList;

    public static PoolDetailResponseDto ofEntity(Pool pool){

        List<PoolImageDto> poolImageDtoList = (pool.getImageList() != null) ?
                pool.getImageList().stream()
                        .map(PoolImageDto::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        List<LessonListResponseDto> lessonListResponseDtoList = (pool.getLessonList() != null) ?
                pool.getLessonList().stream()
                        .map(LessonListResponseDto::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        return PoolDetailResponseDto.builder()
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
                .lessonListResponseDtoList(lessonListResponseDtoList)
                .build();
        }
    }

