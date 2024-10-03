package com.poten.dive_in.academy.controller;

import com.poten.dive_in.academy.dto.AcademyRequestDto;
import com.poten.dive_in.academy.dto.AcademyResponseDto;
import com.poten.dive_in.academy.entity.Academy;
import com.poten.dive_in.academy.service.AcademyService;
import com.poten.dive_in.common.dto.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AcademyController {

    private final AcademyService academyService;

    public AcademyController(AcademyService academyService) {
        this.academyService = academyService;
    }

    @PostMapping("/academies")
    public ResponseEntity<CommonResponse<AcademyResponseDto>> createAcademy(@Valid AcademyRequestDto academyRequestDto, @RequestParam(value = "image",required = false) MultipartFile file){
        AcademyResponseDto academyResponseDto = academyService.createAcademy(academyRequestDto,file);
        return new ResponseEntity<>(CommonResponse.success("업체 등록 성공하였습니다. ",academyResponseDto), HttpStatus.OK);
    }

    @GetMapping("/academies")
    public ResponseEntity<CommonResponse<List<AcademyResponseDto>>> getAcademy(){
        List<AcademyResponseDto> academyResponseDtoList = academyService.getAcademyList();
        return new ResponseEntity<>(CommonResponse.success(null,academyResponseDtoList), HttpStatus.OK);
    }


    @PutMapping("/academies/{id}")
    public ResponseEntity<CommonResponse<AcademyResponseDto>> updateAcademy(@PathVariable Long id, @ModelAttribute AcademyRequestDto academyRequestDto,
                                                                            @RequestParam(value = "image",required = false) MultipartFile file){

        AcademyResponseDto academyResponseDto = academyService.updateAcademy(id,academyRequestDto,file);
        return new ResponseEntity<>(CommonResponse.success("업체 정보 수정에 성공하였습니다.",academyResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/academies/{id}")
    public ResponseEntity<CommonResponse<Object>> deleteAcademy(@PathVariable Long id){
        academyService.deleteAcademy(id);
        return new ResponseEntity<>(CommonResponse.success("업체 삭제 성공하였습니다.",null), HttpStatus.OK);
    }

}
