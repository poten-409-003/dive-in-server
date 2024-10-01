package com.poten.dive_in.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.poten.dive_in.common.exception.S3UploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 s3Client;

    // 여러 개 파일 업로드
    public List<String> uploadFile(List<MultipartFile> multipartFiles){
        if(multipartFiles==null || multipartFiles.isEmpty()){
            throw new IllegalStateException("업로드 할 파일이 없습니다");
        }

        List<String> fileUrlList = new ArrayList<>();
        multipartFiles.forEach(file->{
            if(file.isEmpty()){
                throw new IllegalStateException("빈 파일은 업로드 할 수 없습니다.");
            }

            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());


            try(InputStream inputStream = file.getInputStream()){
                s3Client.putObject(new PutObjectRequest(bucket, fileName,inputStream,objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e){
                throw new S3UploadException("파일 업로드에 실패하였습니다.", e);
            }

            String fileUrl = s3Client.getUrl(bucket, fileName).toString();
            fileUrlList.add(fileUrl);
        });

        return fileUrlList;
    }

    // 파일 삭제
    public void deleteFile(String fileName){
        try{
            s3Client.deleteObject(new DeleteObjectRequest(bucket,fileName));
        }catch (Exception e){
            throw new RuntimeException("파일 삭제 실패했습니다.");
        }
    }

    private String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName){
        if(fileName.isBlank()){
            throw new IllegalArgumentException("파일 이름이 비어있습니다.");
        }
        List<String> fileValidate = List.of(".jpg",".jpeg",".png",".webp");

        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if(!fileValidate.contains(fileExtension)){
            throw new IllegalArgumentException(fileExtension +"는 지원하지 않는 파일 확장자입니다.");
        }
        return fileExtension;
    }
}
