package com.poten.dive_in.community.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poten.dive_in.auth.jwt.JwtTokenProvider;
import com.poten.dive_in.community.post.controller.PostController;
import com.poten.dive_in.community.post.dto.PostDetailResponseDto;
import com.poten.dive_in.community.post.dto.PostEditRequestDto;
import com.poten.dive_in.community.post.dto.PostRequestDto;
import com.poten.dive_in.community.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser
    public void createPost_WithValidInput_Returns201Created() throws Exception {
        // Given
        PostRequestDto requestDto = new PostRequestDto();
        requestDto.setCategoryType("COMMUNICATION");
        requestDto.setTitle("테스트 제목");
        requestDto.setContent("테스트 내용");

        // PostDetailResponseDto Mock 객체 생성
        PostDetailResponseDto mockResponseDto = PostDetailResponseDto.builder()
                .postId(1L)
                .categoryName("소통해요")
                .title("테스트 제목")
                .content("테스트 내용")
                .writer("testUser")
                .viewCnt(0)
                .likesCnt(0)
                .cmmtCnt(0)
                .isLiked(false)
                .isPopular(false)
                .commentList(null)
                .images(null)
                .createdAt("2024-03-07")
                .updatedAt(null)
                .writerProfile(null)
                .build();


        when(postService.createPost(any(PostRequestDto.class), any(), any())).thenReturn(mockResponseDto);

        // JwtTokenProvider Mocking
        when(jwtTokenProvider.validateToken(any(String.class))).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(any(String.class))).thenReturn("test@example.com");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/posts")
                        .contentType(MediaType.APPLICATION_JSON) // Content-Type 유지 (필요에 따라 변경)
                        .param("categoryType", requestDto.getCategoryType()) // 요청 파라미터 설정 (필요에 따라)
                        .param("title", requestDto.getTitle())           // 요청 파라미터 설정 (필요에 따라)
                        .param("content", requestDto.getContent())         // 요청 파라미터 설정 (필요에 따라)
                        .header("Authorization", "Bearer mockToken")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("테스트 제목"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.writer").value("testUser"));

        Mockito.verify(postService).createPost(any(PostRequestDto.class), any(), any());
    }

    @Test
    public void createPost_WithoutToken_Returns401Unauthorized() throws Exception {
        // Given
        PostRequestDto requestDto = new PostRequestDto();
        requestDto.setCategoryType("COMMUNICATION");
        requestDto.setTitle("테스트 제목");
        requestDto.setContent("테스트 내용");

        // String requestBody = objectMapper.writeValueAsString(requestDto); // 더 이상 JSON 요청 본문 불필요

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/posts")
                        .contentType(MediaType.APPLICATION_JSON) // Content-Type 유지 (필요에 따라 변경)
                        // .content(requestBody) // 더 이상 JSON 요청 본문 불필요
                        .param("categoryType", requestDto.getCategoryType()) // 요청 파라미터 설정 (필요에 따라)
                        .param("title", requestDto.getTitle())           // 요청 파라미터 설정 (필요에 따라)
                        .param("content", requestDto.getContent())         // 요청 파라미터 설정 (필요에 따라)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    //
//    @Test
//    @WithMockUser
//    public void deletePost_WithValidInput_Returns204NoContent() throws Exception {
//        // Given
//        Long postIdToDelete = 1L;
//
//        // Mocking
//        Mockito.doNothing().when(postService).deletePost(postIdToDelete, "test@example.com"); // PostService.deletePost() void 메서드 Mocking
//
//        when(jwtTokenProvider.validateToken(any(String.class))).thenReturn(true);
//        when(jwtTokenProvider.getEmailFromToken(any(String.class))).thenReturn("test@example.com");
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.delete("/community/posts/{id}", postIdToDelete) // DELETE 요청, PathVariable 설정
//                        .header("Authorization", "Bearer mockToken")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(MockMvcResultMatchers.status().isNoContent()); // 204 No Content 기대
//
//        Mockito.verify(postService).deletePost(postIdToDelete, "test@example.com"); // PostService.deletePost() 호출 여부 및 파라미터 검증
//    }
//
//    @Test
//    @WithMockUser
//    public void deletePost_PostNotFound_Returns400BadRequest() throws Exception {
//        // Given
//        Long postIdToDelete = 999L; // 존재하지 않는 게시글 ID
//
//        // Mocking
//        Mockito.doThrow(new IllegalArgumentException("Post not found")).when(postService).deletePost(postIdToDelete, "test@example.com"); // PostService.deletePost() 예외 발생 Mocking
//
//        when(jwtTokenProvider.validateToken(any(String.class))).thenReturn(true);
//        when(jwtTokenProvider.getEmailFromToken(any(String.class))).thenReturn("test@example.com");
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.delete("/community/posts/{id}", postIdToDelete)
//                        .header("Authorization", "Bearer mockToken")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest()); // 400 Bad Request 기대
//
//        Mockito.verify(postService).deletePost(postIdToDelete, "test@example.com");
//    }
//
//    @Test
//    @WithMockUser
//    public void updatePost_ValidRequest_Returns200Ok() throws Exception {
//        // Given
//        Long postIdToUpdate = 1L; // 수정할 게시글 ID
//        String mockEmail = "test@example.com";
//
//        // PostEditRequestDto 생성 (PostEditRequestDto에는 Builder가 없으므로 Setter 사용)
//        PostEditRequestDto requestDto = new PostEditRequestDto();
//        requestDto.setCategoryType("정보공유"); // 수정할 카테고리 타입
//        requestDto.setTitle("수정된 제목");       // 수정할 제목
//        requestDto.setContent("수정된 내용");     // 수정할 내용
//        requestDto.setExistingImages("[]");    // 기존 이미지 정보 (수정 시 필요에 따라 설정)
//
//        // MultipartFile Mock 객체 생성 (이미지 수정 시 필요)
//        MockMultipartFile mockFile = new MockMultipartFile("newImages", "test.jpg", "image/jpeg", "test image content".getBytes());
//        List<MultipartFile> multipartFileList = Collections.singletonList(mockFile);
//
//        // PostDetailResponseDto Mock 객체 생성 (수정 후 응답으로 반환될 객체) - @Builder 사용
//        PostDetailResponseDto mockResponseDto = PostDetailResponseDto.builder()
//                .postId(postIdToUpdate)
//                .categoryName("정보공유")
//                .title("수정된 제목")
//                .content("수정된 내용")
//                .writer("testUser")
//                .viewCnt(10)
//                .likesCnt(5)
//                .cmmtCnt(2)
//                .isLiked(true)
//                .isPopular(false)
//                .commentList(null)
//                .images(null)
//                .createdAt("2024-03-07")
//                .updatedAt("2024-03-08") // 수정되었으므로 수정일자 변경
//                .writerProfile(null)
//                .build();
//
//        // JwtTokenProvider Mocking
//        when(jwtTokenProvider.validateToken(any(String.class))).thenReturn(true);
//        when(jwtTokenProvider.getEmailFromToken(any(String.class))).thenReturn(mockEmail);
//
//        // PostService Mocking
//        when(postService.updatePost(eq(postIdToUpdate), any(PostEditRequestDto.class), any(), eq(mockEmail))) // eq() 사용하여 postIdToUpdate 특정 값으로 Mocking
//                .thenReturn(mockResponseDto);
//
//        // When & Then
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/community/posts/{id}", postIdToUpdate) // Path Variable 설정
//                                .contentType(MediaType.MULTIPART_FORM_DATA) // multipart/form-data Content-Type 설정 (파일 업로드 관련)
//                                .header("Authorization", "Bearer mockToken")
//                                .param("categoryType", requestDto.getCategoryType()) // 요청 파라미터 설정 (DTO 필드)
//                                .param("title", requestDto.getTitle())
//                                .param("content", requestDto.getContent())
//                                .param("existingImages", requestDto.getExistingImages())
//
//                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 200 OK 기대
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postId").value(postIdToUpdate)) // 응답 본문 검증
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("수정된 제목"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("수정된 내용"));
//
//
//        Mockito.verify(postService).updatePost(eq(postIdToUpdate), any(PostEditRequestDto.class), any(), eq(mockEmail)); // PostService.updatePost() 호출 여부 및 파라미터 검증
//    }
//
    @Test
    public void updatePost_WithoutToken_Returns401Unauthorized() throws Exception {
        // Given
        Long postIdToUpdate = 1L;
        PostEditRequestDto requestDto = new PostEditRequestDto(); // requestDto 객체 생성 (내용은 중요하지 않음)

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/community/posts/{id}", postIdToUpdate) // Path Variable 설정
                        .contentType(MediaType.MULTIPART_FORM_DATA) // multipart/form-data Content-Type 설정
                        .param("categoryType", requestDto.getCategoryType())
                        .param("title", requestDto.getTitle())
                        .param("content", requestDto.getContent())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()); // 401 Unauthorized 기대
    }
    // deletePost, updatePost 등 다른 엔드포인트에 대한 Mocking 테스트도 유사하게 작성 (팁 참조)
}