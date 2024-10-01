package com.poten.dive_in.academy.service;

import com.poten.dive_in.academy.dto.AcademyRequestDto;
import com.poten.dive_in.academy.dto.AcademyResponseDto;
import com.poten.dive_in.academy.entity.Academy;
import com.poten.dive_in.academy.repository.AcademyRepository;
import com.poten.dive_in.common.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Service
public class AcademyService {

    private final S3Service s3Service;
    private final AcademyRepository academyRepository;


    @Transactional
    public AcademyResponseDto createAcademy(AcademyRequestDto academyRequestDto, MultipartFile file){

        // 업체 프로필 등록을 안했다면
        if (file==null){
            throw new RuntimeException("프로필 이미지를 등록해주세요.");
        }

        // 이미지 스토리지 저장
        List<String> urlList = s3Service.uploadFile(Collections.singletonList(file));
        String profileImageUrl  = urlList.get(0);

        Academy academy = academyRequestDto.toEntity(profileImageUrl);

        Academy savedAcademy = academyRepository.save(academy);

        AcademyResponseDto academyResponseDto = AcademyResponseDto.ofEntity(savedAcademy);

        return academyResponseDto;
    }

    @Transactional(readOnly = true)
    public List<AcademyResponseDto> getAcademyList(){
        List<Academy> academyList = academyRepository.findAll();

        List<AcademyResponseDto> academyResponseDtoList = academyList.stream().map(academy -> AcademyResponseDto.ofEntity(academy)).toList();
        return academyResponseDtoList;
    }


    @Transactional
    public AcademyResponseDto updateAcademy(Long academyId, AcademyRequestDto academyRequestDto, MultipartFile file){

        // id 존재하는지 확인
        Academy academy = academyRepository.findById(academyId)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 업체입니다."));

        String profileImageUrl = null;

        // 이미지 수정이 있는지 확인
        if(file!=null && !file.isEmpty()){
            // 기존 이미지 삭제
            if(academy.getProfileImageUrl()!=null) {
                s3Service.deleteFile(academy.getProfileImageUrl());
            }
            // 새로운 이미지 저장
            List<String> urlList = s3Service.uploadFile(Collections.singletonList(file));
            profileImageUrl  = urlList.get(0);
        }

        academy.updateAcademy(academyRequestDto,profileImageUrl);

        // DTO로 변환 후 반환
        return AcademyResponseDto.ofEntity(academy);
    }


    @Transactional
    public void deleteAcademy(Long academyId){

        // id 존재하는지 확인
        Academy academy = academyRepository.findById(academyId)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 업체입니다."));

        // S3 이미지 삭제
        if(academy.getProfileImageUrl()!=null){
            s3Service.deleteFile(academy.getProfileImageUrl());
        }

        academyRepository.delete(academy);

    }


}
