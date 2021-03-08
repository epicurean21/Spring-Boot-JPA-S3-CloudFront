package kr.co.theplay.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.co.theplay.dto.GalleryDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@NoArgsConstructor
public class S3Service {
    public static final String CLOUD_FRONT_DOMAIN_NAME = "d2tkmpefgqef0b.cloudfront.net";

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(String currentFilePath, MultipartFile file) throws IOException {
        // 고유한 key 값을 갖기위해 현재 시간을 postfix로 붙여줌
        SimpleDateFormat date = new SimpleDateFormat("yyyymmddHHmmss");
        String fileName = file.getOriginalFilename() + "-" + date.format(new Date());

        // key가 존재하면 기존 파일은 삭제
        if ("".equals(currentFilePath) == false && currentFilePath != null) {
            boolean isExistObject = s3Client.doesObjectExist(bucket, currentFilePath);

            if (isExistObject == true) {
                s3Client.deleteObject(bucket, currentFilePath);
            }
        }

        // upload
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 단순 업로드용
        //return s3Client.getUrl(bucket, fileName).toString();
        return fileName; // fileName 변수만 반환,
        // galleryService에서 파일경로를 DB에 저장하기 위한 값으로 사용되는데, 사실은 fileName 변수가 S3객체를 식별하는 key이고 이를 db에 저장

        /*
 CLOUD_FRONT_DOMAIN_NAME
이 상수는 CloudFront 도메인명입니다. ( 배포 생성시 할당된 기본 값을 사용 )
상수 정보를 따로 관리하는 것이 좋을텐데, 예제에서는 간단하게 멤버 변수로 정의했습니다.
따라서 이미지를 조회할 때, S3 URL(s3Client.getUrl())이 아닌 CloudFront URL(CLOUD_FRONT_DOMAIN_NAME)을 사용하게 됩니다.
ex) S3 키 값(fileName 변수)이 sample.jpg라 할 때, 이미지는 "dq582wpwqowa9.cloudfront.net/sample.jpg" 에서 가져오게 됩니다

         */
    }

    public String uploadMultipleFiles (String currentFilePath, MultipartFile file) throws IOException {
        SimpleDateFormat date = new SimpleDateFormat("yyyymmddHHmmss");
        String fileName = file.getOriginalFilename() + "-" + date.format(new Date());

        // upload
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 단순 업로드용
        //return s3Client.getUrl(bucket, fileName).toString();
        return fileName; // fileName 변수만 반환,
    }

    public void delete(String currentFilePath) throws IOException {
        if ("".equals(currentFilePath) == false && currentFilePath != null) {
            boolean isExistObject = s3Client.doesObjectExist(bucket, currentFilePath);

            if (isExistObject == true) {
                s3Client.deleteObject(bucket, currentFilePath);
            }
        }
    }
}
