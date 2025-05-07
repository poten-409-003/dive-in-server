package com.poten.dive_in.lesson.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.instructor.dto.InstructorProfileResponseDto;
import com.poten.dive_in.lesson.dto.*;
import com.poten.dive_in.lesson.dto.SwimClassSearchCondition;
import com.poten.dive_in.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LessonController {

    private final LessonService lessonService;

    /* TODO 수정 필요 */
//    @PostMapping("/lessons")
//    public ResponseEntity<CommonResponse<LessonDetailResponseDto>> createLesson(@Valid LessonRequestDto lessonRequestDto,
//                                                                                @RequestParam(value = "images",required = false) List<MultipartFile> multipartFileList){
//
//        LessonDetailResponseDto lessonResponseDto =lessonService.createLesson(lessonRequestDto, multipartFileList);
//        return new ResponseEntity<>(CommonResponse.success("수업 등록에 성공하였습니다.",lessonResponseDto), HttpStatus.OK);
//
//    }

    @GetMapping("/lessons")
    public ResponseEntity<CommonResponse<List<LessonListResponseDto>>> getLessonList() {
        List<LessonListResponseDto> lessonResponseDtoList = lessonService.getLessonList();
        return new ResponseEntity<>(CommonResponse.success(null, lessonResponseDtoList), HttpStatus.OK);
    }

    @GetMapping("/lessons/{id}")
    public ResponseEntity<CommonResponse<LessonDetailResponseDto>> getLessonDetail(@PathVariable("id") Long id) {
        lessonService.addViewCnt(id);
        LessonDetailResponseDto lessonResponseDto = lessonService.getLessonDetail(id);
        return new ResponseEntity<>(CommonResponse.success(null, lessonResponseDto), HttpStatus.OK);
    }

    /**
     * 특정 수영 클래스의 강사 프로필 목록 조회 (비로그인)
     * GET /api/public/swimclasses/{classId}/instructors
     *
     * @param classId 수영 클래스 ID
     * @return 강사 프로필 목록
     */
    @GetMapping("/{classId}/instructors")
    public ResponseEntity<List<InstructorProfileResponseDto>> getInstructorsBySwimClass(@PathVariable Long classId) {
        List<InstructorProfileResponseDto> instructors = lessonService.getInstructorsBySwimClass(classId);
        return ResponseEntity.ok(instructors);
    }


    /**
     * 수영 클래스 등록 (클래스 운영자 및 강사 회원만)
     * POST /api/swimclasses
     *
     * @param requestDto 수영 클래스 등록 요청 DTO
     * @return 등록된 수영 클래스 상세
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CLASS_OPERATOR', 'INSTRUCTOR')") // 'CLASS_OPERATOR' 또는 'INSTRUCTOR' 역할 허용
    public ResponseEntity<LessonDetailResponseDto> createSwimClass(@RequestBody LessonRequestDto requestDto) {
        LessonDetailResponseDto swimClass = lessonService.createSwimClass(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(swimClass);
    }

    /**
     * 수영 클래스 목록 조회 (클래스 운영자 및 강사 회원만)
     * GET /api/swimclasses
     *
     * @param condition 검색/필터링 조건 DTO
     * @return 수영 클래스 목록 (Page)
     */
    @GetMapping("/{page}")
    @PreAuthorize("hasAnyRole('CLASS_OPERATOR', 'INSTRUCTOR')") // 'CLASS_OPERATOR' 또는 'INSTRUCTOR' 역할 허용
    public ResponseEntity<Page<LessonListResponseDto>> getSwimClassList(
            @ModelAttribute SwimClassSearchCondition condition, @PathVariable Integer page) { // 페이징 정보 자동 바인딩
        Page<LessonListResponseDto> swimClassPage = lessonService.getSwimClassList(condition, page);
        return ResponseEntity.ok(swimClassPage);
    }

    /**
     * 수영 클래스 상세 조회 (클래스 운영자 및 강사 회원만)
     * GET /api/swimclasses/{classId}
     *
     * @param classId 수영 클래스 ID
     * @return 수영 클래스 상세
     */
    @GetMapping("/{classId}")
    @PreAuthorize("hasAnyRole('CLASS_OPERATOR', 'INSTRUCTOR')") // 'CLASS_OPERATOR' 또는 'INSTRUCTOR' 역할 허용
    public ResponseEntity<LessonDetailResponseDto> getSwimClassDetail(@PathVariable Long classId) {
        LessonDetailResponseDto swimClass = lessonService.getSwimClassDetail(classId);
        return ResponseEntity.ok(swimClass);
    }

    /**
     * 수영 클래스 수정 (클래스 운영자 및 강사 회원만)
     * PUT /api/swimclasses/{classId}
     *
     * @param classId    수정할 수영 클래스 ID
     * @param requestDto 수영 클래스 수정 요청 DTO
     * @return 수정된 수영 클래스 상세
     */
    @PutMapping("/{classId}")
    @PreAuthorize("hasAnyRole('CLASS_OPERATOR', 'INSTRUCTOR')") // 'CLASS_OPERATOR' 또는 'INSTRUCTOR' 역할 허용
    public ResponseEntity<LessonDetailResponseDto> updateSwimClass(@PathVariable Long classId, @RequestBody LessonUpdateRequestDto requestDto) {
        LessonDetailResponseDto updatedSwimClass = lessonService.updateSwimClass(classId, requestDto);
        return ResponseEntity.ok(updatedSwimClass);
    }

    /**
     * 수영 클래스 삭제 (클래스 운영자 및 강사 회원만)
     * DELETE /api/swimclasses/{classId}
     *
     * @param classId 삭제할 수영 클래스 ID
     * @return 응답 없음 (No Content)
     */
    @DeleteMapping("/{classId}")
    @PreAuthorize("hasAnyRole('CLASS_OPERATOR', 'INSTRUCTOR')") // 'CLASS_OPERATOR' 또는 'INSTRUCTOR' 역할 허용
    public ResponseEntity<Void> deleteSwimClass(@PathVariable Long classId) {
        lessonService.deleteSwimClass(classId);
        return ResponseEntity.noContent().build();
    }

}
