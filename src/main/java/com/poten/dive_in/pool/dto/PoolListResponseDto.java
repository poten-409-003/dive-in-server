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
public class PoolListResponseDto {
    private Long id;

    private String poolName;

    private String poolAddress;

    private String region;

    private String imageUrl;

    private String latitude;

    private String longitude;

    public static PoolListResponseDto ofEntity(Pool pool){

        String imageUrl = pool.getImageList() != null && !pool.getImageList().isEmpty() ?
                pool.getImageList().get(0).getImageUrl() : null;

        return PoolListResponseDto.builder()
                .id(pool.getId())
                .poolName(pool.getPoolName())
                .poolAddress(pool.getPoolAddress())
                .region(pool.getRegion())
                .imageUrl(imageUrl)
                .latitude(pool.getLatitude())
                .longitude(pool.getLongitude())
                .build();
    }
}
