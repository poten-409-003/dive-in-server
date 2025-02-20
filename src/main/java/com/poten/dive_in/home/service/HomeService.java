package com.poten.dive_in.home.service;

import com.poten.dive_in.community.post.dto.PostHomeResponseDto;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.repository.PostRepository;
import com.poten.dive_in.home.dto.HomeResponseDto;
import com.poten.dive_in.home.dto.ResultDto;
import com.poten.dive_in.lesson.dto.LessonHomeListResponseDto;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.poten.dive_in.lesson.repository.LessonRepository;
import com.poten.dive_in.pool.entity.Pool;
import com.poten.dive_in.pool.repository.PoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final LessonRepository lessonRepository;
    private final PostRepository postRepository;
    private final PoolRepository poolRepository;


    /**
     * 초기 로드 데이터 (HOME)
     * topViewLessonList; //4개
     * newLessonList; //4개
     * topViewPostList; //2개
     * newPostList; //2개
     * competitionPostList; //3개
     */
    public HomeResponseDto getInitialData() {
        List<LessonHomeListResponseDto> topViewLessonList = lessonRepository.findTopViewLessons().stream()
                .map(LessonHomeListResponseDto::ofEntity)
                .collect(Collectors.toList());
        List<LessonHomeListResponseDto> newLessonList = lessonRepository.findNewLessons().stream()
                .map(LessonHomeListResponseDto::ofEntity)
                .collect(Collectors.toList());
        List<PostHomeResponseDto> topViewPostList = postRepository.findTopViewPosts().stream()
                .map(PostHomeResponseDto::ofEntity)
                .collect(Collectors.toList());
        List<PostHomeResponseDto> newPostList = postRepository.findNewPosts().stream()
                .map(PostHomeResponseDto::ofEntity)
                .collect(Collectors.toList());
        List<PostHomeResponseDto> competitionPostList = postRepository.findCompetitionPosts().stream()
                .map(PostHomeResponseDto::ofEntity)
                .collect(Collectors.toList());

        return HomeResponseDto.ofDtoList(
                topViewLessonList,
                newLessonList,
                topViewPostList,
                newPostList,
                competitionPostList
        );
    }

    /**
     * 검색
     * 글 제목, 글 내용, 수영장명, 수영수업명, 수영수업키워드
     * ======조회 결과 Response (키워드, 키워드결과목록)
     * 등록일시 내림차순 (최신순)
     * {키워드, 키워드결과목록}
     * <p>
     * ======결과목록 내 데이터
     * 키워드가 포함된 내용 (내용이 길 경우 일부만),
     * 속한 카테고리(ex. 커뮤니티, 클래스, 수영장),
     * 수영장의 경우 주소도 포함
     */
    public List<ResultDto> getListByKeyword(String keyword) {
        List<ResultDto> result = new ArrayList<>();

        // Post 검색
        List<Post> posts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        result.addAll(posts.stream()
                .map(post -> ResultDto.ofEntity(keyword, post.getTitle(), "커뮤니티", null, post.getCreatedAt()))
                .collect(Collectors.toList()));

        // SwimClass 검색
        List<SwimClass> swimClasses = lessonRepository.findByNameContainingOrKeywordsKeywordCodeNameContaining(keyword, keyword);
        result.addAll(swimClasses.stream()
                .map(swimClass -> ResultDto.ofEntity(keyword, swimClass.getName(), "수영수업", null, swimClass.getCreatedAt()))
                .collect(Collectors.toList()));

        // Pool 검색
        List<Pool> pools = poolRepository.findByNameContaining(keyword);
        result.addAll(pools.stream()
                .map(pool -> ResultDto.ofEntity(keyword, pool.getPoolName(), "수영장", pool.getRoadAddress(), pool.getCreatedAt()))
                .collect(Collectors.toList()));

        // 결과를 등록일시 내림차순으로 정렬
        result.sort((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()));

        return result;
    }
}
