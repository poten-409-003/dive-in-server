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
public class LessonListResponseDto {
    private Long id;
    private String academyName;
    private String academyImageUrl;
    private String keyword;
    private String lessonName;
    private String level;
    private String price;

    public static LessonListResponseDto ofEntity(SwimClass swimClass) {
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
        return LessonListResponseDto.builder()
                .id(swimClass.getClassId())
                .level(level)
                .academyName(swimClass.getInstructorTeam() != null ? swimClass.getInstructorTeam().getName() : null)
                .academyImageUrl(url)
                .keyword(keywords)
                .lessonName(swimClass.getName() != null ? swimClass.getName() : null)
                .price(swimClass.getPrice() != null ? String.valueOf(swimClass.getPrice()) : "가격 문의")
                .build();
    }
}
