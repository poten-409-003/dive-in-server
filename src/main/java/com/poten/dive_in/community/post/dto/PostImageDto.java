package com.poten.dive_in.community.post.dto;

import com.poten.dive_in.community.post.entity.PostImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class PostImageDto {

    private Boolean repImage;
    private String imageUrl;

    public static PostImageDto ofEntity(PostImage postImage) {
        return PostImageDto.builder()
                .repImage("Y".equals(postImage.getIsRepresentative()))
                .imageUrl(postImage.getImageUrl())
                .build();

    }
}

