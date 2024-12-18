package com.poten.dive_in.community.post.dto;

import com.poten.dive_in.community.post.entity.PostImage;
import com.poten.dive_in.pool.entity.PoolImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class PostImageDto {

    private Boolean repImage;

    private String imageUrl;

    public static PostImageDto ofEntity(PostImage postImage){
        return PostImageDto.builder()
                .repImage(postImage.getIsRepresentative()=="Y"? true:false)
                .imageUrl(postImage.getImageUrl())
                .build();

    }
}

