package com.poten.dive_in.lesson.dto;

import com.poten.dive_in.lesson.entity.LessonKeyword;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.poten.dive_in.lesson.entity.SwimClassImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.poten.dive_in.lesson.service.LessonUtils.getLevelsByCode;

@Getter
@Builder
@ToString
public class LessonHomeListResponseDto {
    private Long id;
    private String instructorName;
    private String instructorImgUrl;
    private String keyword;
    private String lessonName;
    private String lessonImgUrl;
    private String level;
    private String price; //보류
    private Integer viewCnt;

    public static LessonHomeListResponseDto ofEntity(SwimClass swimClass) {
        Set<LessonKeyword> lessonKeywords = swimClass.getKeywords();
        String keywords = null;
        if (lessonKeywords != null && lessonKeywords.size() != 0) {
            List<String> keywordList = new ArrayList<>();
            for (LessonKeyword keyword : lessonKeywords) {
                keywordList.add(keyword.getKeyword().getCodeName());
            }
            keywords = String.join(", ", keywordList);
        }

        String level = null;
        if (swimClass.getLevel() != null) {
            level = getLevelsByCode(swimClass.getLevel());
        }
        String url = null;
        if (swimClass.getImages() != null) {
            for (SwimClassImage image : swimClass.getImages()) {
                if (url == null) {
                    url = image.getImageUrl();
                } else if (image.getIsRepresentative() == "Y") {
                    url = image.getImageUrl();
                }
            }
        }
        return LessonHomeListResponseDto.builder()
                .id(swimClass.getClassId())
                .level(level)
                .instructorName(swimClass.getInstructorTeam() != null ? swimClass.getInstructorTeam().getName() : null)
                .instructorImgUrl(swimClass.getInstructorTeam() != null ? swimClass.getInstructorTeam().getProfileImageUrl() : null)
                .keyword(keywords)
                .lessonName(swimClass.getName() != null ? swimClass.getName() : null)
                .lessonImgUrl(url)
                .price(swimClass.getPrice() != null ? String.valueOf(swimClass.getPrice()) : "가격 문의")
                .viewCnt(swimClass.getViewCount() != null ? swimClass.getViewCount() : 0)
                .build();
    }


}
