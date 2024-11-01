package page.clab.api.global.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * {@code QRCodeUtil} 클래스는 QR 코드 이미지를 생성하는 유틸리티 클래스입니다.
 * 주어진 데이터를 인코딩하여 QR 코드 이미지를 생성하고, 바이트 배열로 반환합니다.
 */
public class QRCodeUtil {

    private static final int SIZE = 200;

    public static byte[] encodeQRCode(String data) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, SIZE, SIZE);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }
}
