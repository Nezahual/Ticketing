package my.aiorchestrator.infrastructure.adapters;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import java.io.*;
import lombok.RequiredArgsConstructor;
import my.aiorchestrator.application.ports.outgoing.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3ServiceAdapter implements S3Service {

    private final S3Template s3Template;

    public void pushMultipartFileToBucket(MultipartFile file, String bucket, String s3Key)
            throws IOException {

        s3Template.upload(
                bucket,
                s3Key,
                file.getInputStream(),
                ObjectMetadata.builder().metadata("filename", s3Key).build());
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
