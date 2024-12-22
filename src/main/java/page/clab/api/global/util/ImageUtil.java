package page.clab.api.global.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.global.exception.ImageCompressionException;

/**
 * {@code ImageUtil}은 이미지 파일의 처리와 관련된 유틸리티 메서드를 제공합니다. 이미지의 방향을 조정하거나, 압축을 수행하는 등의 기능을 포함합니다.
 */
@Slf4j
public class ImageUtil {

    /**
     * 이미지 파일의 방향을 조정합니다.
     *
     * @param multipartFile 이미지 파일
     * @return 방향이 조정된 BufferedImage 객체
     * @throws Exception 이미지 처리 중 예외 발생 시
     */
    public static BufferedImage adjustImageDirection(MultipartFile multipartFile) throws Exception {
        Path tempFilePath = TempFileUtil.createSecureTempFile("temp", null);

        File tempFile = tempFilePath.toFile();
        multipartFile.transferTo(tempFile);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(tempFile);
            int originalDirection = getImageDirection(tempFile);
            switch (originalDirection) {
                case 1:
                    break;
                case 3:
                    bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_180, (BufferedImageOp[]) null);
                    break;
                case 6:
                    bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_90, (BufferedImageOp[]) null);
                    break;
                case 8:
                    bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_270, (BufferedImageOp[]) null);
                    break;
            }

            if (tempFile.exists() && !tempFile.delete()) {
                throw new IOException("Failed to delete image file: " + tempFile.getAbsolutePath());
            }
            return bufferedImage;
        } catch (Exception e) {
            return bufferedImage;
        }
    }

    /**
     * 이미지 파일의 방향을 가져옵니다.
     *
     * @param tempFile 임시 이미지 파일
     * @return 이미지 방향 정보
     */
    public static int getImageDirection(File tempFile) throws IOException, ImageProcessingException, MetadataException {
        int originalDirection = 1;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(tempFile);
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null) {
                originalDirection = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (IOException e) {
            log.warn("이미지 파일을 읽는 중 IO 오류 발생: {}", e.getMessage());
            throw e;
        } catch (ImageProcessingException e) {
            log.warn("이미지 파일 처리 중 오류 발생: {}", e.getMessage());
            throw e;
        } catch (MetadataException e) {
            log.warn("이미지 파일의 메타데이터를 읽는 중 오류 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn("예기치 않은 오류 발생: {}", e.getMessage());
            throw e;
        }
        return originalDirection;
    }

    /**
     * 파일이 이미지 파일인지 확인합니다.
     *
     * @param file 파일 객체
     * @return 이미지 파일일 경우 true, 그렇지 않을 경우 false
     */
    public static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * 지정된 품질 수준으로 이미지 파일을 압축합니다.
     * <p>
     * 이 메서드는 지정된 파일 경로에서 이미지를 읽어와, 주어진 품질 설정을 사용하여 압축한 후, 압축된 이미지를 원본 파일에 덮어씁니다. 압축은 이미지 포맷에 따라 다르게 적용되며, 이미지의 형식(JPEG,
     * PNG 등)은 파일 확장자에 의해 결정됩니다.
     *
     * @param filePath 압축할 이미지 파일의 경로.
     * @param quality  압축 품질 수준으로, 0.0(높은 압축률, 낮은 품질)에서 1.0(낮은 압축률, 높은 품질) 사이의 값을 가집니다. 이 값의 정확한 효과는 이미지 포맷에 따라 달라집니다.
     * @throws ImageCompressionException 이미지 압축이 실패한 경우 예외가 발생합니다.
     */
    public static void compressImage(String baseDirectory, String filePath, float quality) {
        try {
            FileUtil.validateFilePath(filePath, baseDirectory);
            File inputFile = new File(filePath);
            BufferedImage image = ImageIO.read(inputFile);
            OutputStream os = new FileOutputStream(inputFile);

            String formatName = FilenameUtils.getExtension(inputFile.getName());
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
            ImageWriter writer = writers.next();

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            ImageIO.write(image, formatName, os);

            os.close();
        } catch (Exception e) {
            throw new ImageCompressionException("이미지 압축 실패 : " + filePath);
        }
    }
}
