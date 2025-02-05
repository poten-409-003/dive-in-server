package com.poten.dive_in.community.post.dto;

import com.poten.dive_in.community.post.entity.PostImage;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

