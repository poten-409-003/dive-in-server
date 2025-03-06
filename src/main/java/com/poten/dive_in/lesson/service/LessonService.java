package com.poten.dive_in.lesson.service;

import com.poten.dive_in.common.service.S3Service;
import com.poten.dive_in.instructor.repository.InstructorRepository;
import com.poten.dive_in.lesson.dto.LessonDetailResponseDto;
import com.poten.dive_in.lesson.dto.LessonListResponseDto;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.poten.dive_in.lesson.entity.SwimClassImage;
import com.poten.dive_in.lesson.repository.CoachingTeamRepository;
import com.poten.dive_in.lesson.repository.LessonRepository;
import com.poten.dive_in.pool.repository.PoolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
