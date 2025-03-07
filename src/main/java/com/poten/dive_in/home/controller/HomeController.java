package com.poten.dive_in.home.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.home.dto.HomeResponseDto;
import com.poten.dive_in.home.dto.ResultDto;
import com.poten.dive_in.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;

    /**
     * 초기 로드 데이터 (HOME) API
     * - topViewLessonList; //4개
     * - newLessonList; //4개
     * - topViewPostList; //2개
     * - newPostList; //2개
     * - competitionPostList; //3개 << TODO 데이터 필요
     *
     * @return HomeResponseDto
     */
    @GetMapping("/initial")
    public ResponseEntity<CommonResponse<HomeResponseDto>> getInitialData() {
        HomeResponseDto homeResponseDto = homeService.getInitialData();
        return ResponseEntity.ok(CommonResponse.success("초기 데이터 로드 성공", homeResponseDto));
    }

    /**
     * 검색 API
     * - 글 제목, 글 내용, 수영장명, 수영수업명, 수영수업키워드 검색
     *
     * @param keyword 검색 키워드
     * @return List<ResultDto> 검색 결과 목록
     */
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ResultDto>>> getListByKeyword(@RequestParam("keyword") String keyword) {
        List<ResultDto> searchResults = homeService.getListByKeyword(keyword);
        if (searchResults == null || searchResults.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(CommonResponse.success("검색 성공", searchResults));
        }
    }
}
