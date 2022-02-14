package com.slub.chamchimayo.upload;

import com.slub.chamchimayo.exception.ExceptionWithCodeAndMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile);
        String uploadFileUrl = uploadToS3(uploadFile);
        return uploadFileUrl;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw ExceptionWithCodeAndMessage.IO_EXCEPTION.getException();
        }
        return file;
    }

    private String uploadToS3(File uploadFile) {

        String fileName = new Date().getTime() + uploadFile.getName();
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .key(fileName)
                .build();
        try {
            amazonS3.putObject(objectRequest, RequestBody.fromFile(uploadFile));
        } catch (SdkClientException e) {
            throw e;
        } finally {
            removeNewFile(uploadFile);
        }
        String url = getURL(amazonS3, bucket, fileName);
        return url;
    }

    private void removeNewFile(File uploadFile) {
        uploadFile.delete();
    }

    public String getURL(S3Client s3, String bucketName, String keyName) {
        URL url = null;
        try {
            GetUrlRequest request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            url = s3.utilities().getUrl(request);
            System.out.println("The URL for " + keyName + " is " + url.toString());
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        } finally {
            return url.toString();
        }
    }



}
