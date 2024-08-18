package page.clab.api.global.common.file.application;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.global.common.file.exception.FileUploadFailException;
import page.clab.api.global.util.FileUtil;
import page.clab.api.global.util.ImageCompressionUtil;
import page.clab.api.global.util.LogSanitizerUtil;
import page.clab.api.global.util.TempFileUtil;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Component
@Configuration
@Slf4j
public class FileHandler {

    private final Set<String> disallowExtensions = new HashSet<>();
    private final Set<String> compressibleImageExtensions = new HashSet<>();

    @Value("${resource.file.path}")
    private String filePath;

    @Value("${resource.file.image-quality}")
    private float imageQuality;

    public FileHandler(
            @Value("${resource.file.disallow-extension}") String[] disallowExtensions,
            @Value("${resource.file.compressible-image-extension}") String[] compressibleImageExtensions
    ) {
        this.disallowExtensions.addAll(Arrays.asList(disallowExtensions));
        this.compressibleImageExtensions.addAll(Arrays.asList(compressibleImageExtensions));
    }

    public void init() {
        filePath = filePath.replace("/", File.separator).replace("\\", File.separator);
    }

    public void saveQRCodeImage(byte[] image, String category, String saveFilename, String extension) throws IOException {
        init();
        String savePath = filePath + File.separator + category + File.separator + saveFilename;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        File file = new File(savePath);
        FileUtil.ensureParentDirectoryExists(file);
        ImageIO.write(bufferedImage, extension, file);
    }

    public String saveFile(MultipartFile multipartFile, String category) throws IOException {
        init();
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        FileUtil.validateFileAttributes(originalFilename, disallowExtensions);

        String saveFilename = makeFileName(extension);
        String savePath = filePath + File.separator + category + File.separator + saveFilename;

        File file = new File(savePath);
        FileUtil.ensureParentDirectoryExists(file);

        try {
            if (isImageFile(multipartFile)) {
                BufferedImage originalImage = adjustImageDirection(multipartFile);
                ImageIO.write(originalImage, Objects.requireNonNull(extension), file);
            } else {
                multipartFile.transferTo(file);
            }
        } catch (Exception e) {
            throw new IOException("이미지의 뱡향을 조정하는 데 오류가 발생했습니다.", e);
        }

        setFilePermissions(file, savePath, extension);
        return savePath;
    }

    private BufferedImage adjustImageDirection(MultipartFile multipartFile) throws Exception {
        Path tempFilePath = TempFileUtil.createSecureTempFile("temp", null);

        File tempFile = tempFilePath.toFile();
        multipartFile.transferTo(tempFile);

        int originalDirection = getImageDirection(tempFile);
        BufferedImage bufferedImage = ImageIO.read(tempFile);

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
    }

    public int getImageDirection(File tempFile) {
        int originalDirection = 1;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(tempFile);
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null) {
                originalDirection = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (IOException e) {
            log.error("이미지 파일을 읽는 중 IO 오류 발생: {}", e.getMessage());
        } catch (ImageProcessingException e) {
            log.error("이미지 파일 처리 중 오류 발생: {}", e.getMessage());
        } catch (MetadataException e) {
            log.error("이미지 파일의 메타데이터를 읽는 중 오류 발생: {}", e.getMessage());
        } catch (Exception e) {
            log.error("예기치 않은 오류 발생: {}", e.getMessage());
        }
        return originalDirection;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public String makeFileName(String extension) {
        return System.nanoTime() + "_" + UUID.randomUUID() + "." + extension;
    }

    private void setFilePermissions(File file, String savePath, String extension) throws FileUploadFailException {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (compressibleImageExtensions.contains(extension.toLowerCase())) {
                ImageCompressionUtil.compressImage(savePath, imageQuality);
            }
            if (os.contains("win")) {
                boolean readOnly = file.setReadOnly();
                if (!readOnly) {
                    log.error("Failed to set file read-only: {}", LogSanitizerUtil.sanitizeForLog(savePath));
                }
            } else {
                FileUtil.setReadOnlyPermissionsUnix(savePath);
            }
        } catch (Exception e) {
            throw new FileUploadFailException("Failed to upload file: " + savePath, e);
        }
    }

    public void deleteFile(String savedPath) {
        File fileToDelete = new File(savedPath);
        boolean deleted = fileToDelete.delete();
        if (!deleted) {
            log.error("Failed to delete file: {}", savedPath);
        }
    }
}
