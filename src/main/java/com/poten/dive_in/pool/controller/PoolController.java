package com.poten.dive_in.pool.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.pool.dto.PoolDetailResponseDto;
import com.poten.dive_in.pool.dto.PoolListResponseDto;
import com.poten.dive_in.pool.dto.PoolRequestDto;
import com.poten.dive_in.pool.service.PoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PoolController {

    private final PoolService poolService;
    private final ObjectMapper objectMapper;

    @PostMapping("/pools")
    public ResponseEntity<CommonResponse<PoolDetailResponseDto>> createPool(@Valid PoolRequestDto poolRequestDto, @RequestParam(value = "images", required = false) List<MultipartFile> multipartFileList) {
        PoolDetailResponseDto poolResponseDto = poolService.createPool(poolRequestDto, multipartFileList);
        return new ResponseEntity<>(CommonResponse.success("수영장 등록 성공하였습니다.", poolResponseDto), HttpStatus.OK);
    }

    @GetMapping("/pools")
    public ResponseEntity<CommonResponse<List<PoolListResponseDto>>> getPoolList() {
        List<PoolListResponseDto> poolListResponseDtos = poolService.getPoolList();
        return new ResponseEntity<>(CommonResponse.success(null, poolListResponseDtos), HttpStatus.OK);
    }

    // 상세 조회
    @GetMapping("/pools/{id}")
    public ResponseEntity<CommonResponse<PoolDetailResponseDto>> getPool(@PathVariable("id") Long id) throws JsonProcessingException {
        PoolDetailResponseDto poolResponseDto = poolService.getPool(id);
        String responseData = objectMapper.writeValueAsString(poolResponseDto);
        return new ResponseEntity<>(CommonResponse.success(null, poolResponseDto), HttpStatus.OK);
    }

    @PutMapping("/pools/{id}")
    public ResponseEntity<CommonResponse<PoolDetailResponseDto>> updatePool(@PathVariable("id") Long id,
                                                                            @Valid PoolRequestDto poolRequestDto,
                                                                            @RequestParam(value = "images", required = false) List<MultipartFile> multipartFileList) {

        PoolDetailResponseDto poolResponseDto = poolService.updatePool(id, poolRequestDto, multipartFileList);
        return new ResponseEntity<>(CommonResponse.success(null, poolResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/pools/{id}")
    public ResponseEntity<CommonResponse<PoolDetailResponseDto>> deletePool(@PathVariable("id") Long id) {

        poolService.deletePool(id);
        return new ResponseEntity<>(CommonResponse.error("수영장 삭제 완료되었습니다.", null), HttpStatus.OK);

    }

}
