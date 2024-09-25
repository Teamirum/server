package server.domain.image.service;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.domain.image.dto.ImageResponseDto;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.nio.ByteBuffer;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final MemberRepository memberRepository;
    private final Storage storage;

    private static final String DEFAULT_IMG_DIR = "img/";
    private static final String DEFAULT_DOMAIN = "https://storage.googleapis.com/";

    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;
//
    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;




    public ImageResponseDto.ImageUploadSuccessResponseDto uploadImg(MultipartFile image, String memberId) {
        Member member = getMemberByMemberId(memberId);

        String uuid = UUID.randomUUID().toString();
        String fileDir = member.getIdx() + "/" + DEFAULT_IMG_DIR;
        String ext = image.getContentType();
        String fileName = fileDir + uuid + image.getOriginalFilename() + "." + ext;

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(ext)
                .build();
        try (WriteChannel writer = storage.writer(blobInfo)) {
            byte[] imageData = image.getBytes(); // 이미지 데이터를 byte 배열로 읽어옵니다.
            writer.write(ByteBuffer.wrap(imageData));
        } catch (Exception ex) {
            // 예외 처리 코드
            ex.printStackTrace();
            log.info("이미지 업로드 실패: " + ex.getMessage());
            throw new ErrorHandler(ErrorStatus.IMAGE_UPLOAD_FAIL);
        }
        String imgUlr = DEFAULT_DOMAIN + bucketName + "/" + fileName;
        return ImageResponseDto.ImageUploadSuccessResponseDto.builder()
                .imgUrl(imgUlr)
                .build();
    }

    private Member getMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
