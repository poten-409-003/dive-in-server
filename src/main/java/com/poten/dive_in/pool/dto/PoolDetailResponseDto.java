package com.poten.dive_in.pool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poten.dive_in.lesson.dto.LessonListResponseDto;
import com.poten.dive_in.pool.entity.Pool;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter @Builder @ToString
public class PoolDetailResponseDto {

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

    @JsonProperty("poolImages")
    private List<PoolImageDto> poolImageDtoList;

    @JsonProperty("lessons")
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
                .closingDays(pool.getClosingDays())
                .latitude(pool.getLatitude())
                .longitude(pool.getLongitude())
                .contact(pool.getContact())
                .laneLength(pool.getLaneLength())
                .laneCount(pool.getLaneCount())
                .maxDepth(pool.getMaxDepth())
                .minDepth(pool.getMinDepth())
                .facilities(pool.getFacilities())
                .region(pool.getRegion())
                .poolImageDtoList(poolImageDtoList)
                .lessonListResponseDtoList(lessonListResponseDtoList)
                .build();
        }
    }

