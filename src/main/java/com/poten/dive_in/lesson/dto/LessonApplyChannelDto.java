package com.poten.dive_in.lesson.dto;

import com.poten.dive_in.lesson.entity.ApplicationMethod;
import com.poten.dive_in.lesson.entity.SwimClass;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonApplyChannelDto {

    private String applyUrl;
    private String applyUrlType;

    public static LessonApplyChannelDto ofEntity(ApplicationMethod applicationMethod) {
        return LessonApplyChannelDto.builder()
                .applyUrl(applicationMethod.getApplicationUrl())
                .applyUrlType(applicationMethod.getApplicationType().getCodeName())
                .build();
    }

    /*TODO 최종체크 때 필요없는 거 맞는지 확인*/
//    public ApplicationMethod toEntity(SwimClass swimClass) {
//        return ApplicationMethod.builder()
//                .applicationUrl(this.applyUrlType)
//                .swimClass(swimClass)
//                .build();
//    }
}
