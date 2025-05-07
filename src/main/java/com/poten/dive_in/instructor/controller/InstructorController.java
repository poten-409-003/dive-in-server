package com.poten.dive_in.instructor.controller;

import org.springframework.web.bind.annotation.RestController;

import com.poten.dive_in.instructor.dto.InstructorCreateRequestDto;
import com.poten.dive_in.instructor.dto.InstructorProfileResponseDto;
import com.poten.dive_in.instructor.dto.InstructorUpdateRequestDto;
import com.poten.dive_in.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // 권한 체크 어노테이션
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/instructors") // 강사 회원만 접근 가능한 경로
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    /**
     * 강사 프로필 등록 (강사 회원만)
     * POST /api/instructors
     *
     * @param requestDto 강사 프로필 등록 요청 DTO
     * @return 등록된 강사 프로필
     */
    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')") // 'INSTRUCTOR' 역할만 허용
    public ResponseEntity<InstructorProfileResponseDto> createInstructorProfile(@RequestBody InstructorCreateRequestDto requestDto, Principal principal) {
        InstructorProfileResponseDto instructorProfile = instructorService.createInstructorProfile(principal.getName(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(instructorProfile);
    }

    /**
     * 로그인한 강사 회원의 프로필 조회
     * GET /api/instructors/me
     *
     * @return 강사 프로필
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('INSTRUCTOR')") // 'INSTRUCTOR' 역할만 허용
    public ResponseEntity<InstructorProfileResponseDto> getMyInstructorProfile(Principal principal) {
        InstructorProfileResponseDto instructorProfile = instructorService.getMyInstructorProfile(principal.getName());
        return ResponseEntity.ok(instructorProfile);
    }

    /**
     * 강사 프로필 수정 (강사 회원 본인만)
     * PUT /api/instructors/{instructorId}
     *
     * @param instructorId 수정할 강사 프로필 ID
     * @param requestDto   강사 프로필 수정 요청 DTO
     * @return 수정된 강사 프로필
     */
    @PutMapping("/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR')") // 'INSTRUCTOR' 역할만 허용
    public ResponseEntity<InstructorProfileResponseDto> updateInstructorProfile(@PathVariable Long instructorId, @RequestBody InstructorUpdateRequestDto requestDto, Principal principal) {
        InstructorProfileResponseDto updatedProfile = instructorService.updateInstructorProfile(instructorId, principal.getName(), requestDto);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * 강사 프로필 삭제 (강사 회원 본인만)
     * DELETE /api/instructors/{instructorId}
     *
     * @param instructorId 삭제할 강사 프로필 ID
     * @return 응답 없음 (No Content)
     */
    @DeleteMapping("/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR')") // 'INSTRUCTOR' 역할만 허용
    public ResponseEntity<Void> deleteInstructorProfile(@PathVariable Long instructorId, Principal principal) {
        instructorService.deleteInstructorProfile(instructorId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}

