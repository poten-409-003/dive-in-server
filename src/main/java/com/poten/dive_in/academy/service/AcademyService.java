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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static com.poten.dive_in.common.service.S3Service.extractFileName;


@RequiredArgsConstructor
@Service
public class AcademyService {

    private final S3Service s3Service;
    private final AcademyRepository academyRepository;


    @Transactional
    public AcademyResponseDto createAcademy(AcademyRequestDto academyRequestDto, MultipartFile file){

        String profileImageUrl = null;

        // 이미지가 있는 경우만 업로드
        if (file != null && !file.isEmpty()) {
            List<String> urlList = s3Service.uploadFile(Collections.singletonList(file));
            profileImageUrl  = urlList.get(0);
        }


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
    public AcademyResponseDto updateAcademy(Long academyId, AcademyRequestDto academyRequestDto, @RequestParam(value = "image",required = false) MultipartFile file){

        // id 존재하는지 확인
        Academy academy = academyRepository.findById(academyId)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 업체입니다."));

        String profileImageUrl = null;

        // 이미지 수정이 있는지 확인
        if(file!=null && !file.isEmpty()){
            // 기존 이미지 삭제
            if(academy.getProfileImageUrl()!=null) {
                String fileName = extractFileName(academy.getProfileImageUrl());
                s3Service.deleteFile(fileName);
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
            String fileName = extractFileName(academy.getProfileImageUrl());
            s3Service.deleteFile(fileName);
        }

        academyRepository.delete(academy);

    }



}
