package server.domain.businessCard.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    // 기존의 ByteArrayOutputStream 생성 메서드 (필요하다면 사용)
    public ByteArrayOutputStream generateQRCode(String data) throws WriterException, IOException {
        // QR 코드 생성
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter()
                .encode(data, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);

        // QR 코드를 이미지로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream;
    }
}
