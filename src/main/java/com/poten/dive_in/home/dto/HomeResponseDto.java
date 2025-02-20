package com.poten.dive_in.home.dto;

import com.poten.dive_in.community.post.dto.PostHomeResponseDto;
import com.poten.dive_in.lesson.dto.LessonHomeListResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponseDto {
    private List<LessonHomeListResponseDto> topViewLessonList; //4개

    private List<LessonHomeListResponseDto> newLessonList; //4개

    private List<PostHomeResponseDto> topViewPostList; //2개

    private List<PostHomeResponseDto> newPostList; //2개

    private List<PostHomeResponseDto> competitionPostList; //3개

    public static HomeResponseDto ofDtoList(List<LessonHomeListResponseDto> topViewLessonList, List<LessonHomeListResponseDto> newLessonList, List<PostHomeResponseDto> topViewPostList, List<PostHomeResponseDto> newPostList, List<PostHomeResponseDto> competitionPostList) {
        return HomeResponseDto.builder()
                .topViewLessonList(topViewLessonList)
                .newLessonList(newLessonList)
                .topViewPostList(topViewPostList)
                .newPostList(newPostList)
                .competitionPostList(competitionPostList)
                .build();
    }
}
