package page.clab.api.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QRCodeUtil {

    @Value("${resource.file.path}")
    private String filePath;

    private static final int SIZE = 200;

  /*  public static void encodeQRCode(String url) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, SIZE, SIZE);

        Path path = FileSystems.getDefault().getPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }*/

    public static byte[] encodeQRCode(String data) throws WriterException, IOException{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, SIZE, SIZE);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    public static String decodeQRCode(byte[] qrCodeImage) throws IOException, NotFoundException, ChecksumException, FormatException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(qrCodeImage);
        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

        if (bufferedImage == null) {
            throw new NullPointerException("QRCode의 이미지를 불러올 수 없습니다.");
        }
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        QRCodeReader reader = new QRCodeReader();
        Result result = reader.decode(bitmap);

        return result.getText();
    }

}
