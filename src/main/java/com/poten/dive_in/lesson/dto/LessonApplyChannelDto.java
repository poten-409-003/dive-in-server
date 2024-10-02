package com.poten.dive_in.lesson.dto;

import com.poten.dive_in.lesson.entity.Lesson;
import com.poten.dive_in.lesson.entity.LessonApplyChannel;
import com.poten.dive_in.lesson.enums.LessonChannelType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonApplyChannelDto {

    private String applyUrl;
    private LessonChannelType applyUrlType;

    public static LessonApplyChannelDto ofEntity(LessonApplyChannel lessonApplyChannel) {
        return LessonApplyChannelDto.builder()
                .applyUrl(lessonApplyChannel.getApplyUrl())
                .applyUrlType(lessonApplyChannel.getApplyUrlType())
                .build();
    }

    public LessonApplyChannel toEntity(Lesson lesson) {
        return LessonApplyChannel.builder()
                .applyUrl(this.applyUrl)
                .applyUrlType(this.applyUrlType)
                .lesson(lesson)
                .build();
    }
}
