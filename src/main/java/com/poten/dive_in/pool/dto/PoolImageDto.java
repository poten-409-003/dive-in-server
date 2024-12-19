package com.poten.dive_in.pool.dto;

import com.poten.dive_in.pool.entity.PoolImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class PoolImageDto {

    private Boolean repImage;

    private String imageUrl;

    public static PoolImageDto ofEntity(PoolImage poolImage){
        return PoolImageDto.builder()
                .repImage(poolImage.getRprsImgYn()=="Y"? true:false)
                .imageUrl(poolImage.getImgUrl())
                .build();

    }
}
