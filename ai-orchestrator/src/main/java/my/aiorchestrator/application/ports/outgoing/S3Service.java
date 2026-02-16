package my.aiorchestrator.application.ports.outgoing;

import io.awspring.cloud.s3.S3Resource;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    void pushMultipartFileToBucket(MultipartFile file, String bucket) throws IOException;

    S3Resource downloadFromBucket(String bucket, String key);

    void deleteFileFromBucket(String bucket, String key);
}
