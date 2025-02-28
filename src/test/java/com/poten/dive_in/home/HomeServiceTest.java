package com.poten.dive_in.home;

import com.poten.dive_in.community.post.dto.PostHomeResponseDto;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.repository.PostRepository;
import com.poten.dive_in.home.dto.HomeResponseDto;
import com.poten.dive_in.home.dto.ResultDto;
import com.poten.dive_in.home.service.HomeService;
import com.poten.dive_in.lesson.dto.LessonHomeListResponseDto;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.poten.dive_in.lesson.repository.LessonRepository;
import com.poten.dive_in.pool.entity.Pool;
import com.poten.dive_in.pool.repository.PoolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest // Spring Boot Test Context 로드
@ExtendWith(MockitoExtension.class) // Mockito 사용 설정
class HomeServiceTest {

    @InjectMocks // HomeService 에 MockBean 으로 주입된 Mock 객체들을 주입
    private HomeService homeService;

    @MockBean // Mock 객체로 Spring Context 에 등록, HomeService 에 주입됨
    private LessonRepository lessonRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private PoolRepository poolRepository;

    @Test
    void getInitialData_정상_호출() {
        // Given ( Mock Repository 행동 정의 )
        when(lessonRepository.findTopViewLessons()).thenReturn(createMockLessonListEntity(4));
        when(lessonRepository.findNewLessons()).thenReturn(createMockLessonListEntity(4));
        when(postRepository.findTopViewPosts()).thenReturn(createMockPostListEntity(2));
        when(postRepository.findNewPosts()).thenReturn(createMockPostListEntity(2));
        when(postRepository.findCompetitionPosts()).thenReturn(createMockPostListEntity(3));

        // When ( HomeService 메서드 호출 )
        HomeResponseDto homeResponseDto = homeService.getInitialData();

        // Then ( 반환값 검증 )
        assertNotNull(homeResponseDto);
        assertEquals(4, homeResponseDto.getTopViewLessonList().size());
        assertEquals(4, homeResponseDto.getNewLessonList().size());
        assertEquals(2, homeResponseDto.getTopViewPostList().size());
        assertEquals(2, homeResponseDto.getNewPostList().size());
        assertEquals(3, homeResponseDto.getCompetitionPostList().size());
    }

    @Test
    void getListByKeyword_정상_호출() {
        // Given ( Mock Repository 행동 정의 )
        String keyword = "수영";
        when(postRepository.findByKeyword(keyword)).thenReturn(createMockPostListEntity(2));
        when(lessonRepository.findByKeyword(keyword)).thenReturn(createMockLessonListEntity(2));
        when(poolRepository.findByKeyword(keyword)).thenReturn(createMockPoolList(2));

        // When ( HomeService 메서드 호출 )
        List<ResultDto> resultDtoList = homeService.getListByKeyword(keyword);

        // Then ( 반환값 검증 )
        assertNotNull(resultDtoList);
        assertEquals(6, resultDtoList.size()); // Post 2 + Lesson 2 + Pool 2 = 6

        // 각 ResultDto 내용 검증 (예시: 첫 번째 Post 결과)
        ResultDto postResult = resultDtoList.get(0);
        assertEquals(keyword, postResult.getKeyword());
        assertEquals("Post 제목 0", postResult.getContentTitle());
        assertEquals("커뮤니티", postResult.getCategoryName());
        assertNull(postResult.getAddress());
        assertTrue(postResult.getContentSummary().startsWith("...Post 내용 0...")); // contentSummary 시작 문자열 검증

        // 추가적인 ResultDto 내용 검증 (필요에 따라 Lesson, Pool 결과도 검증)
        // ...
    }


    // Mock 데이터 생성 Helper 메서드

    private List<LessonHomeListResponseDto> createMockLessonList(int size) {
        return Arrays.asList(new LessonHomeListResponseDto[size]); // 간단하게 size 만큼 채운 List 생성
    }

    private List<PostHomeResponseDto> createMockPostList(int size) {
        List<PostHomeResponseDto> mockList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mockList.add(PostHomeResponseDto.builder()
                    .title("Post 제목 " + i)
                    .content("Post 내용 " + i + " ... (contentSummary 테스트용)")
                    .build());
        }
        return mockList;
    }

    private List<SwimClass> createMockLessonListEntity(int size) {
        List<SwimClass> mockList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mockList.add(SwimClass.builder()
                    .name("Lesson 이름 " + i)
                    .introduction("Lesson 소개 " + i + " ... (contentSummary 테스트용)")
                    .build());
        }
        return mockList;
    }

    private List<Post> createMockPostListEntity(int size) {
        List<Post> mockList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mockList.add(Post.builder()
                    .title("Post 제목 " + i)
                    .content("Post 내용 " + i + " ... (contentSummary 테스트용)")
                    .build());
        }
        return mockList;
    }


    private List<Pool> createMockPoolList(int size) {
        List<Pool> mockList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mockList.add(Pool.builder()
                    .poolName("Pool 이름 " + i)
                    .roadAddress("Pool 주소 " + i)
                    .operatingHours("Pool 운영시간 " + i + " ... (contentSummary 테스트용)")
                    .build());
        }
        return mockList;
    }
}
