package com.poten.dive_in.lesson.dto;

import com.poten.dive_in.lesson.entity.SwimClassImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonImageDto {

    private String imageUrl;

    public static LessonImageDto ofEntity(SwimClassImage lessonImage) {
        return LessonImageDto.builder()
                .imageUrl(lessonImage.getImageUrl())
                .build();
    }
}