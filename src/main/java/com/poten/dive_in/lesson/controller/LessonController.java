package com.poten.dive_in.lesson.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.lesson.dto.LessonDetailResponseDto;
import com.poten.dive_in.lesson.dto.LessonListResponseDto;
import com.poten.dive_in.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
