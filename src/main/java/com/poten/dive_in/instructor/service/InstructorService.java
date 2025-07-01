package com.poten.dive_in.instructor.service;

import com.poten.dive_in.auth.entity.Member; // Member 엔티티 필요
import com.poten.dive_in.auth.repository.MemberRepository; // MemberRepository 필요
import com.poten.dive_in.instructor.dto.InstructorCreateRequestDto;
import com.poten.dive_in.instructor.dto.InstructorProfileResponseDto;
import com.poten.dive_in.instructor.dto.InstructorUpdateRequestDto;
import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.instructor.repository.InstructorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorService {

    private final InstructorRepository instructorRepository; // Spring Data JPA Repository
    private final MemberRepository memberRepository; // MemberRepository 필요 (로그인한 회원 정보 조회용)

    /**
     * 강사 프로필을 등록합니다. (강사 회원만 가능)
     *
     * @param email      로그인한 회원 email
     * @param requestDto 강사 프로필 등록 요청 DTO
     * @return 등록된 강사 프로필 DTO
     */
    public InstructorProfileResponseDto createInstructorProfile(String email, InstructorCreateRequestDto requestDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 이미 강사 프로필이 있는지 확인
        instructorRepository.findByMemberId(member.getId()).ifPresent(instructor -> {
            throw new EntityExistsException("이미 존재하는 프로필입니다.");
        });

        // Instructor 엔티티 생성 및 정보 매핑
        Instructor instructor = Instructor.builder()
                .member(member) // 회원과 연결
                .name(requestDto.getName())
                .isAthlete(requestDto.getIsAthlete())
                .awards(requestDto.getAwards())
                .certifications(requestDto.getCertifications())
                .description(requestDto.getDescription())
                .isActive("Y") // 기본 활성화
                .build();

        // 저장
        Instructor savedInstructor = instructorRepository.save(instructor);

        return InstructorProfileResponseDto.ofEntity(savedInstructor); // DTO 변환
    }

    /**
     * 로그인한 강사 회원의 프로필을 조회합니다.
     *
     * @param email 로그인한 회원 email
     * @return 강사 프로필 DTO
     */
    @Transactional(readOnly = true)
    public InstructorProfileResponseDto getMyInstructorProfile(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 회원 ID를 통해 강사 프로필 조회 (Custom Repository 활용)
        Instructor instructor = instructorRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 프로필입니다.")); // 프로필 없음.

        // 필요한 연관 엔티티 함께 조회 (예: 팀 정보) - findWithTeamById 사용 가능
        // Instructor instructor = instructorRepository.findWithTeamById(instructor.getId())
        //         .orElseThrow(() -> new CustomException(ErrorCode.INSTRUCTOR_PROFILE_NOT_FOUND));


        return InstructorProfileResponseDto.ofEntity(instructor); // DTO 변환
    }

    /**
     * 강사 프로필을 수정합니다. (강사 회원 본인만 가능)
     *
     * @param instructorId 수정할 강사 프로필 ID
     * @param email        로그인한 회원 email
     * @param requestDto   강사 프로필 수정 요청 DTO
     * @return 수정된 강사 프로필 DTO
     */
    public InstructorProfileResponseDto updateInstructorProfile(Long instructorId, String email, InstructorUpdateRequestDto requestDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 강사 프로필 조회
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 프로필입니다.")); // 프로필 없음.

        // 로그인한 회원이 해당 강사 프로필의 소유자인지 확인
        if (!instructor.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다."); // 권한 없음
        }

        // DTO 정보 반영하여 엔티티 수정
        instructor.updateInstructor(requestDto.getName(), requestDto.getIsAthlete(), requestDto.getAwards(), requestDto.getCertifications(), requestDto.getDescription());

        // 별도의 save 호출 없이 트랜잭션 종료 시 자동 반영 (Dirty Checking)

        return InstructorProfileResponseDto.ofEntity(instructor); // DTO 변환
    }

    /**
     * 강사 프로필을 삭제합니다. (강사 회원 본인만 가능)
     *
     * @param instructorId 삭제할 강사 프로필 ID
     * @param email        로그인한 회원 email
     */
    public void deleteInstructorProfile(Long instructorId, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 강사 프로필 조회
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 프로필입니다.")); // 프로필 없음.

        // 로그인한 회원이 해당 강사 프로필의 소유자인지 확인
        if (!instructor.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다."); // 권한 없음
        }

        // 삭제 (또는 isActive 상태 변경)
        instructorRepository.delete(instructor);
        // instructor.setIsActive("N"); // 논리적 삭제 시
    }

}

