package com.poten.dive_in.home;

import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.repository.PostRepository;
import com.poten.dive_in.home.dto.HomeResponseDto;
import com.poten.dive_in.home.dto.ResultDto;
import com.poten.dive_in.home.service.HomeService;
import com.poten.dive_in.lesson.entity.LessonKeyword;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.poten.dive_in.lesson.repository.LessonRepository;
import com.poten.dive_in.pool.entity.Pool;
import com.poten.dive_in.pool.repository.PoolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 사용을 위한 Extension
class HomeServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PoolRepository poolRepository;

    @InjectMocks // @Mock으로 생성된 Mock 객체들을 주입
    private HomeService homeService;

//    @Test
//    void getInitialData_정상_반환() {
//        // Given
//        List<SwimClass> mockLessons = Arrays.asList(
//                SwimClass.builder()
//                        .classId(1L)
//                        .name("수영 수업1")
//                        .introduction("소개1")
//                        .viewCount(10)
//                        .build(),
//                SwimClass.builder()
//                        .classId(2L)
//                        .name("수영 수업2")
//                        .introduction("소개2")
//                        .viewCount(5)
//                        .build()
//        );
//        List<Post> mockPosts = Arrays.asList(
//                Post.builder()
//                        .id(1L)
//                        .title("게시글1")
//                        .content("내용1")
//                        .viewCount(20)
//                        .build(),
//                Post.builder()
//                        .id(2L)
//                        .title("게시글2")
//                        .content("내용2")
//                        .viewCount(15)
//                        .build()
//        );
//
//        when(lessonRepository.findNewLessons()).thenReturn(mockLessons); // findNewLessons() 호출 시 mockLessons 반환하도록 설정
//        when(postRepository.findTopViewPosts()).thenReturn(mockPosts); // findTopViewPosts() 호출 시 mockPosts 반환하도록 설정
//        when(postRepository.findNewPosts()).thenReturn(mockPosts); // findNewPosts() 호출 시 mockPosts 반환하도록 설정
//        when(postRepository.findCompetitionPosts()).thenReturn(mockPosts); // findCompetitionPosts() 호출 시 mockPosts 반환하도록 설정
//
//        // When
//        HomeResponseDto responseDto = homeService.getInitialData();
//
//        // Then
//        assertNotNull(responseDto);
//        assertEquals(2, responseDto.getTopViewLessonList().size());
//        assertEquals(2, responseDto.getNewLessonList().size());
//        assertEquals(2, responseDto.getTopViewPostList().size());
//        assertEquals(2, responseDto.getNewPostList().size());
//        assertEquals(2, responseDto.getCompetitionPostList().size());
//
//        // 각 리스트의 첫 번째 요소 검증 (데이터 매핑 검증)
//        LessonHomeListResponseDto topViewLessonDto = responseDto.getTopViewLessonList().get(0);
//        assertEquals("수영 수업1", topViewLessonDto.getLessonName());
//        PostHomeResponseDto topViewPostDto = responseDto.getTopViewPostList().get(0);
//        assertEquals("게시글1", topViewPostDto.getTitle());
//
//        // Repository 메서드 호출 횟수 검증 (Mockito.verify)
//        verify(lessonRepository, times(2)).findNewLessons(); // findNewLessons() 2번 호출 확인 (topViewLessonList, newLessonList)
//        verify(postRepository, times(1)).findTopViewPosts();
//        verify(postRepository, times(1)).findNewPosts();
//        verify(postRepository, times(1)).findCompetitionPosts();
//    }

    @Test
    void getInitialData_DB_데이터_없음() {
        // Given
        when(lessonRepository.findNewLessons()).thenReturn(Collections.emptyList());
        when(postRepository.findTopViewPosts()).thenReturn(Collections.emptyList());
        when(postRepository.findNewPosts()).thenReturn(Collections.emptyList());
        when(postRepository.findCompetitionPosts()).thenReturn(Collections.emptyList());

        // When
        HomeResponseDto responseDto = homeService.getInitialData();

        // Then
        assertNotNull(responseDto);
        assertTrue(responseDto.getTopViewLessonList().isEmpty());
        assertTrue(responseDto.getNewLessonList().isEmpty());
        assertTrue(responseDto.getTopViewPostList().isEmpty());
        assertTrue(responseDto.getNewPostList().isEmpty());
        assertTrue(responseDto.getCompetitionPostList().isEmpty());
    }


    @Test
    void getListByKeyword_정상_검색() {
        // Given
        String keyword = "검색어";
        List<Post> mockPosts = Arrays.asList(
                Post.builder()
                        .id(3L)
                        .title("검색어 포함 게시글")
                        .content("내용에 검색어 포함")
                        .build()
        );
        Set<LessonKeyword> keywords = Set.of(
                LessonKeyword.builder()
                        .id(1L)
                        .keyword(CommonCode.builder()
                                .codeName("검색어 키워드1")
                                .build())
                        .swimClass(SwimClass.builder().classId(1L).build())
                        .build(),
                LessonKeyword.builder()
                        .id(2L)
                        .keyword(CommonCode.builder()
                                .codeName("검색어 키워드2")
                                .build())
                        .swimClass(SwimClass.builder().classId(2L).build())
                        .build()
        );
        List<SwimClass> mockSwimClasses = Arrays.asList(
                SwimClass.builder()
                        .classId(4L)
                        .name("검색어 수업")
                        .keywords(keywords)
                        .build()
        );
        List<Pool> mockPools = Arrays.asList(
                Pool.builder()
                        .poolId(5L)
                        .poolName("검색어 수영장")
                        .roadAddress("주소")
                        .build()
        );

        when(postRepository.findByKeyword(keyword)).thenReturn(mockPosts);
        when(lessonRepository.findByKeyword(keyword)).thenReturn(mockSwimClasses);
        when(poolRepository.findByKeyword(keyword)).thenReturn(mockPools);

        // When
        List<ResultDto> resultDtoList = homeService.getListByKeyword(keyword);

        // Then
        assertNotNull(resultDtoList);
        assertEquals(3, resultDtoList.size());

        // Post 결과 검증
        ResultDto postResult = resultDtoList.stream().filter(dto -> dto.getCategoryName().equals("커뮤니티")).findFirst().orElse(null);
        assertNotNull(postResult);
        assertEquals("검색어 포함 게시글", postResult.getTitle());
        assertTrue(postResult.getContentSummary().contains("검색어")); // extractSummary 검증

        // SwimClass 결과 검증
        ResultDto swimClassResult = resultDtoList.stream().filter(dto -> dto.getCategoryName().equals("수영수업")).findFirst().orElse(null);
        assertNotNull(swimClassResult);
        assertEquals("검색어 수업", swimClassResult.getTitle());
        assertTrue(swimClassResult.getContentSummary().contains("검색어")); // extractSummary 검증

        // Pool 결과 검증
        ResultDto poolResult = resultDtoList.stream().filter(dto -> dto.getCategoryName().equals("수영장")).findFirst().orElse(null);
        assertNotNull(poolResult);
        assertEquals("검색어 수영장", poolResult.getTitle());
        assertEquals("주소", poolResult.getContent()); // Pool 주소 검증
        assertTrue(poolResult.getContentSummary().contains("수영장")); // extractSummary 검증 (Pool 운영시간 내용 사용)

        verify(postRepository, times(1)).findByKeyword(keyword);
        verify(lessonRepository, times(1)).findByKeyword(keyword);
        verify(poolRepository, times(1)).findByKeyword(keyword);
    }

    @Test
    void getListByKeyword_검색어_결과_없음() {
        // Given
        String keyword = "없는검색어";
        when(postRepository.findByKeyword(keyword)).thenReturn(Collections.emptyList());
        when(lessonRepository.findByKeyword(keyword)).thenReturn(Collections.emptyList());
        when(poolRepository.findByKeyword(keyword)).thenReturn(Collections.emptyList());

        // When
        List<ResultDto> resultDtoList = homeService.getListByKeyword(keyword);

        // Then
        assertNull(resultDtoList);
    }

    @Test
    void extractSummary_키워드_있음() {
        // Given
        String content = "긴 내용 긴 내용 긴 내용 긴 내용 긴 내용 긴 내용 긴 내용 긴 내용 긴 내용 검색어 긴 내용 긴 내용 긴 내용 긴 내용";
        String keyword = "검색어";

        // When
        String summary = homeService.extractSummary(content, keyword);

        // Then
        String expectedSummary = "...긴 내용 긴 내용 긴 내용 검색어 긴 내용 긴 내용 긴 내용..."; // 정확한 기대값
        assertEquals(expectedSummary, summary); // assertEquals로 정확하게 비교
    }
}