package server.domain.businessCard.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.domain.image.service.ImageService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class QRCodeService {

    private final ImageService imageService;
    private static final int QR_CODE_WIDTH = 300;  // QR 코드의 너비
    private static final int QR_CODE_HEIGHT = 300; // QR 코드의 높이

    // QR 코드를 생성하는 메서드
    public ByteArrayOutputStream generateQRCode(String data) throws WriterException, IOException {
        // 데이터를 UTF-8로 인코딩하여 QR 코드 생성
        BitMatrix bitMatrix = new QRCodeWriter()
                .encode(new String(data.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1),
                        BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);

        // QR 코드를 이미지로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream;
    }

    // QR 코드를 생성한 후 업로드하고 URL을 반환하는 메서드
    public String generateAndUploadQRCode(String data, Long businessCardIdx) throws WriterException, IOException {
        // QR 코드 생성
        ByteArrayOutputStream qrCodeStream = generateQRCode(data);

        // ByteArrayOutputStream을 MultipartFile로 변환
        MultipartFile qrCodeImage = new ByteArrayMultipartFile(
                "qrCode.png",                   // 파일 이름
                "qrCode.png",                   // 원본 파일 이름
                "image/png",                    // 콘텐츠 타입
                qrCodeStream.toByteArray()      // byte 배열로 변환
        );

        // 생성된 QR 코드를 업로드하고 URL 반환
        return imageService.uploadQrImg(qrCodeImage, businessCardIdx);
    }

    public String getOrderRoomQRImage(Long orderIdx, Long marketIdx, String memberId) throws WriterException, IOException {
        String baseUrl = "http://localhost:5173/payment-waiting"; // 기본 URL
        String queryParam = "?orderIdx=" + orderIdx + "&marketIdx=" + marketIdx; // idx를 쿼리 파라미터로 추가
        String fullUrl = baseUrl + queryParam; // 완전한 URL 생성

        String info = new String(String.format(
                "{\"url\":\"%s\"}", fullUrl
        ).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        ByteArrayOutputStream qrCodeStream = generateQRCode(info);

        // ByteArrayOutputStream을 MultipartFile로 변환
        MultipartFile qrCodeImage = new ByteArrayMultipartFile(
                "qrCode.png",                   // 파일 이름
                "qrCode.png",                   // 원본 파일 이름
                "image/png",                    // 콘텐츠 타입
                qrCodeStream.toByteArray()      // byte 배열로 변환
        );

        return imageService.uploadImg(qrCodeImage, memberId).getImgUrl();
    }

    // MultipartFile을 직접 구현한 클래스
    public class ByteArrayMultipartFile implements MultipartFile {

        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public ByteArrayMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getOriginalFilename() {
            return this.originalFilename;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Override
        public boolean isEmpty() {
            return this.content.length == 0;
        }

        @Override
        public long getSize() {
            return this.content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return this.content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            throw new UnsupportedOperationException("This method is not supported.");
        }
    }
}
