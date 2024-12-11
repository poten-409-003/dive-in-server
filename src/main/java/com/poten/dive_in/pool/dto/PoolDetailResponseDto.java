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
    private String zipCode; //우편번호

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

        String[] addrs = pool.getRdNmAddr().split(" ");
        String region = addrs[0] + addrs[1];

        return PoolDetailResponseDto.builder()
                .id(pool.getPoolId())
                .poolName(pool.getPoolNm())
                .poolAddress(pool.getRdNmAddr())
                .zipCode(pool.getZipCd())
                .operatingHours(pool.getOprtHr())
                .closingDays(pool.getDyOfDay())
                .latitude(String.valueOf(pool.getLttd()))
                .longitude(String.valueOf(pool.getHrdn()))
                .contact(pool.getTelno())
                .laneLength(Integer.valueOf(pool.getLaneLngt()))
                .laneCount(Integer.valueOf(pool.getLaneCnt()))
                .maxDepth(pool.getMaxDpt())
                .minDepth(pool.getMinDpt())
                .facilities(pool.getAmntCd())
                .region(region)
                .poolImageDtoList(poolImageDtoList)
                .lessonListResponseDtoList(lessonListResponseDtoList)
                .build();
        }
    }

