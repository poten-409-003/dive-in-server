package com.poten.dive_in.lesson.service;

import com.poten.dive_in.common.service.S3Service;
import com.poten.dive_in.instructor.dto.InstructorProfileResponseDto;
import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.instructor.repository.InstructorRepository;
import com.poten.dive_in.lesson.dto.*;
import com.poten.dive_in.lesson.dto.SwimClassSearchCondition;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.poten.dive_in.lesson.entity.SwimClassImage;
import com.poten.dive_in.lesson.repository.CoachingTeamRepository;
import com.poten.dive_in.lesson.repository.LessonRepository;
import com.poten.dive_in.pool.repository.PoolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.poten.dive_in.common.service.S3Service.extractFileName;


@RequiredArgsConstructor
@Service
public class LessonService {

    private final S3Service s3Service;

    private final LessonRepository lessonRepository;
    private final InstructorRepository instructorRepository;
    private final CoachingTeamRepository coachingTeamRepository;
    private final PoolRepository poolRepository;


    @Transactional(readOnly = true)
    public List<LessonListResponseDto> getLessonList() {
        List<SwimClass> swimClassList = lessonRepository.findAll();
        return swimClassList.stream().map(LessonListResponseDto::ofEntity).toList();
    }

    @Transactional(readOnly = true)
    public LessonDetailResponseDto getLessonDetail(Long lessonId) {
        SwimClass swimClass = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 수업입니다."));

        return LessonDetailResponseDto.ofEntity(swimClass);
    }

    @Transactional
    public void addViewCnt(Long lessonId) {
        SwimClass swimClass = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 수업입니다."));

        swimClass.assignViewCnt(1);
    }


    /**
     * 특정 수영 클래스의 강사 프로필 목록을 조회합니다. (비로그인 가능)
     *
     * @param classId 수영 클래스 ID
     * @return 강사 프로필 DTO 목록
     */
    public List<InstructorProfileResponseDto> getInstructorsBySwimClass(Long classId) {
        // Custom Repository를 사용하여 N+1 문제 없이 강사 엔티티 목록 조회
        List<Instructor> instructors = lessonRepository.findInstructorsByClassId(classId);

        // 조회된 강사 엔티티 목록을 DTO 목록으로 변환
        return instructors.stream()
                .map(InstructorProfileResponseDto::ofEntity) // InstructorProfileResponseDto::ofEntity 메서드 필요
                .collect(Collectors.toList());
    }

    /**
     * 수영 클래스를 등록합니다. (클래스 운영자 및 강사 회원만 가능)
     *
     * @param requestDto 수영 클래스 등록 요청 DTO
     * @return 등록된 수영 클래스 상세 DTO
     */
    public LessonDetailResponseDto createSwimClass(LessonRequestDto requestDto) {
        // DTO 정보를 기반으로 SwimClass 엔티티 생성
        SwimClass swimClass = SwimClass.builder()
                .name(requestDto.getLessonName())
                .level(requestDto.getLevel())
                .participantCount(requestDto.getCapacity())
                .price(requestDto.getPrice())
                .operatingHours(requestDto.getLessonSchedule())
                .subject(requestDto.getSubject())
                .introduction(requestDto.getLessonIntroduction())
                .isActive("Y") // 기본 활성화
                // instructorTeam, pool 등 연관 엔티티는 ID를 받아와서 설정 필요
                // .instructorTeam(...)
                // .pool(...)
                .build();

        SwimClass savedSwimClass = lessonRepository.save(swimClass);

        SwimClass detailSwimClass = lessonRepository.findDetailByClassId(savedSwimClass.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("클래스 등록 실패.")); // 저장 후 조회 실패 시

        return LessonDetailResponseDto.ofEntity(detailSwimClass);
    }

    /**
     * 수영 클래스 목록을 조회합니다. (클래스 운영자 및 강사 회원만 가능)
     *
     * @param condition 검색/필터링 조건 DTO
     * @return 수영 클래스 목록 (Page of DTO)
     */
    @Transactional(readOnly = true)
    public Page<LessonListResponseDto> getSwimClassList(SwimClassSearchCondition condition, Integer page) {
        int sizePerPage = 10;
        Pageable pageable = PageRequest.of(page, sizePerPage);
        Page<SwimClass> swimClassPage = lessonRepository.findSwimClasses(condition, pageable);
        return swimClassPage.map(LessonListResponseDto::ofEntity); // SwimClassListResponseDto::ofEntity 메서드 필요
    }

    /**
     * 수영 클래스 상세 정보를 조회합니다. (클래스 운영자 및 강사 회원만 가능)
     *
     * @param classId 수영 클래스 ID
     * @return 수영 클래스 상세 DTO
     */
    @Transactional(readOnly = true)
    public LessonDetailResponseDto getSwimClassDetail(Long classId) {
        // @EntityGraph를 활용한 상세 조회 (N+1 방지)
        SwimClass swimClass = lessonRepository.findDetailByClassId(classId)
                .orElseThrow(() -> new IllegalArgumentException("해당 클래스가 존재하지 않습니다.")); // 클래스 없음

        return LessonDetailResponseDto.ofEntity(swimClass); // DTO 변환
    }

    /**
     * 수영 클래스 정보를 수정합니다. (클래스 운영자 및 강사 회원만 가능)
     *
     * @param classId    수정할 수영 클래스 ID
     * @param requestDto 수영 클래스 수정 요청 DTO
     * @return 수정된 수영 클래스 상세 DTO
     */
    public LessonDetailResponseDto updateSwimClass(Long classId, LessonUpdateRequestDto requestDto) {
        // 수영 클래스 조회
        SwimClass swimClass = lessonRepository.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("해당 클래스가 존재하지 않습니다.")); // 클래스 없음

        // TODO: 로그인한 사용자가 해당 클래스를 수정할 권한이 있는지 확인, 클래스 등록자 컬럼 필요
//         if (!swimClass.getRegisteredMember().getId().equals(currentMemberId)) {
//             throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
//         }

        // DTO 정보 반영하여 엔티티 수정
        // SwimClass 엔티티에 update 메서드 추가 권장
        // swimClass.update(requestDto);
//        swimClass.setName(requestDto.getName());
//        swimClass.setLevel(requestDto.getLevel());
        // ... 다른 필드들 업데이트 ...

        // 연관 엔티티 (이미지, 자격, 정책 등) 수정/삭제/추가 로직 처리


        // 별도의 save 호출 없이 트랜잭션 종료 시 자동 반영 (Dirty Checking)

        // 수정된 엔티티를 상세 조회하여 DTO로 변환 (연관 엔티티 정보 포함)
        SwimClass updatedSwimClass = lessonRepository.findDetailByClassId(classId)
                .orElseThrow(() -> new EntityNotFoundException("해당 클래스 수정 실패. 존재하지 않습니다."));

        return LessonDetailResponseDto.ofEntity(updatedSwimClass);
    }

    /**
     * 수영 클래스를 삭제합니다. (클래스 운영자 및 강사 회원만 가능)
     *
     * @param classId 삭제할 수영 클래스 ID
     */
    public void deleteSwimClass(Long classId) {
        // 수영 클래스 조회
        SwimClass swimClass = lessonRepository.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("해당 클래스가 존재하지 않습니다.")); // 클래스 없음

        // TODO: 로그인한 사용자가 해당 클래스를 삭제할 권한이 있는지 확인

        // 삭제 (또는 isActive 상태 변경)
        lessonRepository.delete(swimClass);
        // swimClass.setIsActive("N"); // 논리적 삭제 시
    }


    /* TODO 수정 필요 */
//    @Transactional
//    public LessonDetailResponseDto createLesson(LessonRequestDto lessonRequestDto, List<MultipartFile> multipartFileList){
//
//        // Academy 존재 여부 확인
//
//        //코칭팀 존재 여부 확인
//        CoachingTeam team = lessonRequestDto.getCoachTeamId() != null ?
//                coachingTeamRepository.findById(lessonRequestDto.getCoachTeamId())
//                        .orElseThrow(() -> new EntityNotFoundException("등록되지 않은 코치팀입니다.")) : null;
//
//        // Pool 존재 여부 확인
//        Pool pool = lessonRequestDto.getPoolId() != null ?
//                poolRepository.findById(lessonRequestDto.getPoolId())
//                        .orElseThrow(() -> new EntityNotFoundException("등록되지 않은 수영장입니다.")) : null;
//
//        // Instructor 목록 확인
//        List<Instructor> instructors = new ArrayList<>();
//        if (lessonRequestDto.getInstructorIdList() != null && !lessonRequestDto.getInstructorIdList().isEmpty()) {
//            instructors = instructorRepository.findAllById(lessonRequestDto.getInstructorIdList());
//            if (instructors.isEmpty()) {
//                throw new EntityNotFoundException("등록되지 않은 강사가 존재합니다.");
//            }
//        }
//
//        // Level 존재 확인 및 생성
//
//
//        // Lesson 엔티티 생성
//        SwimClass swimClass = lessonRequestDto.toEntity();
//
//
////        if (team != null) swimClass.assignCoachingTeam(team);
//
////        if (!instructors.isEmpty()) {
////            List<Instructor> instructorList = new ArrayList<>();
////            for (Instructor instructor : instructors) {
////                Instructor lessonInstructor = Instructor.builder()
////                        .swimClass(swimClass)
////                        .instructor(instructor)
////                        .build();
////                lessonInstructorList.add(lessonInstructor);
////            }
////            swimClass.assignInstructors(lessonInstructorList);  // Instructor 설정
////        }
//
//        // Apply Channel 설정 (있을 때만 처리)
//        if (lessonRequestDto.getApplyChannelDtoList() != null && !lessonRequestDto.getApplyChannelDtoList().isEmpty()) {
//            List<ApplicationMethod> applyChannels = lessonRequestDto.getApplyChannelDtoList().stream()
//                    .map(dto -> dto.toEntity(swimClass))
//                    .collect(Collectors.toList());
//            swimClass.assignApplyChannels(applyChannels);
//        }
//
//
//        // 이미지가 있는 경우만
//        if (multipartFileList != null && !multipartFileList.isEmpty()){
//            List<SwimClassImage> lessonImageList = uploadAndCreateLessonImages(multipartFileList, swimClass);
//            swimClass.addImage(lessonImageList);
//        }
//
//        if (pool != null) {
//            swimClass.assignPool(pool);
//            pool.addLesson(swimClass);
//        }
//
//        lessonRepository.save(swimClass);
//
//        return LessonDetailResponseDto.ofEntity(swimClass);
//
//    }


    // Lesson 리스트 생성 및 S3 업로드 처리
    private List<SwimClassImage> uploadAndCreateLessonImages(List<MultipartFile> multipartFileList, SwimClass swimClass) {
        List<SwimClassImage> lessonImageList = new ArrayList<>();
        List<String> uploadFileList = s3Service.uploadFile(multipartFileList);

        //첫번째 이미지를 대표로 지정
        for (int i = 0; i < uploadFileList.size(); i++) {

            SwimClassImage lessonImage = SwimClassImage.builder()
                    .imageUrl(uploadFileList.get(i))
                    .swimClass(swimClass)
                    .isRepresentative(i == 0 ? "Y" : "N")
                    .build();
            lessonImageList.add(lessonImage);
        }

        return lessonImageList;
    }

    // S3에 저장된 이미지 삭제 처리
    private void deleteLessonImagesFromS3(List<SwimClassImage> lessonImageList) {
        if (lessonImageList != null && !lessonImageList.isEmpty()) {
            lessonImageList.forEach(lessonImage -> {
                String fileName = extractFileName(lessonImage.getImageUrl());
                s3Service.deleteFile(fileName);
            });
        }
    }
}
