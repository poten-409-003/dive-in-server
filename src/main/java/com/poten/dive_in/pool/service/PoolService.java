package com.poten.dive_in.pool.service;

import com.poten.dive_in.common.service.S3Service;
import com.poten.dive_in.pool.dto.PoolRequestDto;
import com.poten.dive_in.pool.dto.PoolResponseDto;
import com.poten.dive_in.pool.entity.Pool;
import com.poten.dive_in.pool.entity.PoolImage;
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
public class PoolService {

    private final S3Service s3Service;
    private final PoolRepository poolRepository;

    @Transactional
    public PoolResponseDto createPool(PoolRequestDto poolRequestDto, List<MultipartFile> multipartFileList){

        // DTO -> 엔티티 변환
        Pool pool = poolRequestDto.toEntity();

        // 이미지가 있는 경우만
        if (multipartFileList !=null && !multipartFileList.isEmpty()){

            List<PoolImage> poolImageList = uploadAndCreatePoolImages(multipartFileList,pool);

            pool.addImage(poolImageList);
        }

        // cascade 설정으로 Pool만 저장해도 Image까지 저장됨
        poolRepository.save(pool);

        return PoolResponseDto.ofEntity(pool);
    }

    @Transactional(readOnly = true)
    public List<PoolResponseDto> getPoolList() {
        List<Pool> poolList = poolRepository.findAll();
        return poolList.stream().map(PoolResponseDto::ofEntity).toList();
    }

    @Transactional(readOnly = true)
    public PoolResponseDto getPool(Long poolId){
        Pool pool = poolRepository.findById(poolId).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 수영장입니다."));
        return PoolResponseDto.ofEntity(pool);
    }

    @Transactional
    public PoolResponseDto updatePool(Long poolId, PoolRequestDto poolRequestDto, List<MultipartFile> multipartFileList) {

        Pool pool = poolRepository.findById(poolId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 수영장입니다."));

        pool.updatePool(poolRequestDto);

        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            deletePoolImagesFromS3(pool.getImageList());
            List<PoolImage> poolImageList = uploadAndCreatePoolImages(multipartFileList, pool);
            pool.replaceImageList(poolImageList);
        }

        return PoolResponseDto.ofEntity(pool);
    }

    @Transactional
    public void deletePool(Long poolId){
        Pool pool = poolRepository.findById(poolId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 수영장입니다."));

        deletePoolImagesFromS3(pool.getImageList());
        poolRepository.delete(pool);
    }

    // PoolImage 리스트 생성 및 S3 업로드 처리
    private List<PoolImage> uploadAndCreatePoolImages(List<MultipartFile> multipartFileList, Pool pool) {
        List<PoolImage> poolImageList = new ArrayList<>();
        List<String> uploadFileList = s3Service.uploadFile(multipartFileList);

        for (int i = 0; i < uploadFileList.size(); i++) {
            boolean repImage = (i == 0);

            PoolImage poolImage = PoolImage.builder()
                    .imageUrl(uploadFileList.get(i))
                    .repImage(repImage)
                    .pool(pool)
                    .build();
            poolImageList.add(poolImage);
        }

        return poolImageList;
    }

    // S3에 저장된 이미지 삭제 처리
    private void deletePoolImagesFromS3(List<PoolImage> poolImageList) {
        if (poolImageList != null && !poolImageList.isEmpty()) {
            poolImageList.forEach(poolImage -> {
                String fileName = extractFileName(poolImage.getImageUrl());
                s3Service.deleteFile(fileName);
            });
        }
    }

}
