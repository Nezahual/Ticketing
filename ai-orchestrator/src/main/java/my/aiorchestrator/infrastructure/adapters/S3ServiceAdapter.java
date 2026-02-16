package my.aiorchestrator.infrastructure.adapters;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import my.aiorchestrator.application.ports.outgoing.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.*;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class S3ServiceAdapter implements S3Service {

    private final S3Template s3Template;

    public void pushMultipartFileToBucket(MultipartFile file, String bucket) throws IOException {

        s3Template.upload(bucket, file.getName(), file.getInputStream(), ObjectMetadata.builder().metadata("filename", file.getName()).build());
    }

    @Override
    public S3Resource downloadFromBucket(String bucket, String key) {

        return s3Template.download(bucket, key);
    }

    @Override
    public void deleteFileFromBucket(String bucket, String key) {

        s3Template.deleteObject(bucket, key);
    }
}
