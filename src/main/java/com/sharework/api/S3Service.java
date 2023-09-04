package com.sharework.api;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${profile.title}")
    private String saveTitle;

    @Value("${profile.subTitle}")
    private String saveSubTitle;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile multipartFile,long userId){

        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.split("\\.")[1];
        String contentType = "";

        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            default:
                contentType = "image/jpg";
                break;
        }
        String title = saveTitle + "_" + saveSubTitle + "_" + userId + '.' + ext;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3.putObject(new PutObjectRequest(bucket, title, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return title;
    }

    public String getObjectUrl(String title){
        return amazonS3.getUrl(bucket, title).toString();
    }

    public void deleteFile(String fileName){
        amazonS3.deleteObject(bucket, fileName);
    }

}
