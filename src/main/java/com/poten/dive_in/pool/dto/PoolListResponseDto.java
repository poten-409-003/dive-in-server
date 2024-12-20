package com.poten.dive_in.pool.dto;

import com.poten.dive_in.pool.entity.Pool;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PoolListResponseDto {
    private Long id;

    private String poolName;

    private String poolAddress;

    private String region;
//    private String zipCode;

    private String imageUrl;

    private String latitude;

    private String longitude;

    public static PoolListResponseDto ofEntity(Pool pool) {

        String imageUrl = pool.getImageList() != null && !pool.getImageList().isEmpty() ?
                pool.getImageList().get(0).getImgUrl() : null;
        String[] addrs = pool.getRoadAddress().split(" ");
        String region = addrs[0] + addrs[1];
        return PoolListResponseDto.builder()
                .id(pool.getPoolId())
                .poolName(pool.getPoolName())
                .poolAddress(pool.getRoadAddress())
                .region(region)
                .imageUrl(imageUrl)
                .latitude(String.valueOf(pool.getLatitude()))
                .longitude(String.valueOf(pool.getLongitude()))
                .build();
    }
}
