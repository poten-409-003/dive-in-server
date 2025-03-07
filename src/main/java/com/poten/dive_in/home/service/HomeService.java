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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final LessonRepository lessonRepository;
    private final PostRepository postRepository;
    private final PoolRepository poolRepository;

    @Transactional
    public HomeResponseDto getInitialData() {
        List<LessonHomeListResponseDto> topViewLessonList = getLessonHomeListDtoList(lessonRepository.findTopViewLessons());
        List<LessonHomeListResponseDto> newLessonList = getLessonHomeListDtoList(lessonRepository.findNewLessons());
        List<PostHomeResponseDto> topViewPostList = getPostHomeResponseDtoList(postRepository.findTopViewPosts());
        List<PostHomeResponseDto> newPostList = getPostHomeResponseDtoList(postRepository.findNewPosts());
        List<PostHomeResponseDto> competitionPostList = getPostHomeResponseDtoList(postRepository.findCompetitionPosts());

        return HomeResponseDto.ofDtoList(
                topViewLessonList,
                newLessonList,
                topViewPostList,
                newPostList,
                competitionPostList
        );
    }

    private List<LessonHomeListResponseDto> getLessonHomeListDtoList(List<SwimClass> lessons) {
        return lessons.stream()
                .map(LessonHomeListResponseDto::ofEntity)
                .collect(Collectors.toList());
    }

    private List<PostHomeResponseDto> getPostHomeResponseDtoList(List<Post> posts) {
        return posts.stream()
                .map(PostHomeResponseDto::ofEntity)
                .collect(Collectors.toList());
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
    @Transactional
    public List<ResultDto> getListByKeyword(String keyword) {
        List<ResultDto> result = new ArrayList<>();

        List<Post> posts = postRepository.findByKeyword(keyword);
        result.addAll(posts.stream()
                .map(post -> {
                    String content = post.getContent();
                    String title = post.getTitle();
                    String contentSummary = extractSummary(content, keyword);
                    String resultTitle = "";

                    boolean keywordInTitle = title != null && title.toLowerCase().contains(keyword.toLowerCase());
                    boolean keywordInContent = content != null && content.toLowerCase().contains(keyword.toLowerCase());

                    if (keywordInTitle && keywordInContent) {
                        // 제목과 내용 모두에 키워드 포함: 게시글 제목 사용
                        resultTitle = title;
                    } else if (!keywordInTitle && keywordInContent) {
                        resultTitle = "게시글 내용 중 검색결과";
                    } else if (keywordInTitle && !keywordInContent) {
                        resultTitle = title;
                        contentSummary = ""; // 내용 요약 비움
                    }

                    return ResultDto.ofEntity(keyword, resultTitle, "커뮤니티", null, post.getCreatedAt(), contentSummary);
                })
                .collect(Collectors.toList()));


        List<SwimClass> swimClasses = lessonRepository.findByKeyword(keyword);
        result.addAll(swimClasses.stream()
                .map(swimClass -> {
                    String description = swimClass.getIntroduction();
                    String contentSummary = extractSummary(description, keyword);
                    String swimClassName = swimClass.getName();
                    String keywords = swimClass.getKeyword();
                    String resultTitle = "";

                    boolean keywordInName = swimClassName != null && swimClassName.toLowerCase().contains(keyword.toLowerCase());
                    boolean keywordInKeywords = keywords != null && keywords.toLowerCase().contains(keyword.toLowerCase());

                    if (keywordInName && keywordInKeywords) {
                        // 수업 이름과 키워드 모두에 키워드 포함: 수업 이름 사용
                        resultTitle = swimClassName;
                    } else if (!keywordInName && keywordInKeywords) {
                        resultTitle = "수영 수업 키워드 검색 결과";
                    } else if (keywordInName && !keywordInKeywords) {
                        resultTitle = swimClassName;
                        contentSummary = "";
                    }

                    return ResultDto.ofEntity(keyword, resultTitle, "수영수업", null, swimClass.getCreatedAt(), contentSummary);
                })
                .collect(Collectors.toList()));

        List<Pool> pools = poolRepository.findByKeyword(keyword);
        result.addAll(pools.stream()
                .map(pool -> {
                    String poolDescription = pool.getOperatingHours(); // 운영시간을 내용으로 사용, 필요에 따라 수정
                    String contentSummary = extractSummary(poolDescription, keyword);
                    return ResultDto.ofEntity(keyword, pool.getPoolName(), "수영장", pool.getRoadAddress(), pool.getCreatedAt(), contentSummary);
                })
                .collect(Collectors.toList()));

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public String extractSummary(String content, String keyword) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        int keywordIndex = content.toLowerCase().indexOf(keyword.toLowerCase()); // case-insensitive 검색
        if (keywordIndex != -1) {
            int startIndex = Math.max(0, keywordIndex - 15);
            int endIndex = Math.min(content.length(), keywordIndex + keyword.length() + 30);

            int summaryLength = endIndex - startIndex;
            if (summaryLength < 30 && content.length() > 30) {
                endIndex = Math.min(content.length(), startIndex + 30);
            }

            String summary = content.substring(startIndex, endIndex);
            System.out.println("Keyword: " + keyword); // 로그 추가
            System.out.println("Keyword Index: " + keywordIndex); // 로그 추가
            System.out.println("StartIndex: " + startIndex); // 로그 추가
            System.out.println("EndIndex: " + endIndex); // 로그 추가
            System.out.println("Summary Length: " + summary.length()); // 로그 추가
            System.out.println("Extracted Summary: " + summary); // 로그 추가


            return "..." + summary + "...";
        } else {
            return content.length() > 100 ? content.substring(0, 100) + "..." : content; // 키워드 없으면 기존 요약 방식 유지
        }
    }

}
