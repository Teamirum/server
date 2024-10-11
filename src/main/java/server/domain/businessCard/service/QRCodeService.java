package server.domain.businessCard.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class QRCodeService {

    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;

    // QR 코드 이미지를 BufferedImage 형태로 생성하는 메서드
    public BufferedImage generateQRCodeImage(String data) throws WriterException, IOException {
        // QR 코드 생성
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter()
                .encode(data, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);

        // QR 코드를 BufferedImage로 변환
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    // ByteArrayOutputStream으로 QR 코드를 생성하여 반환하는 메서드
    public ByteArrayOutputStream generateQRCode(String data) throws WriterException, IOException {
        // 데이터를 그대로 UTF-8로 인코딩하여 QR 코드 생성
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter()
                .encode(new String(data.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), // 이 부분에서 ISO-8859-1로 변환
                        BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);

        // QR 코드를 이미지로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream;
    }

}
