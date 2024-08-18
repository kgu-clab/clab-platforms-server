package page.clab.api.global.common.file.application;

import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.global.util.FileUtil;
import page.clab.api.global.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

        String saveFilename = FileUtil.makeFileName(extension);
        String savePath = filePath + File.separator + category + File.separator + saveFilename;

        File file = new File(savePath);
        FileUtil.ensureParentDirectoryExists(file);

        try {
            if (ImageUtil.isImageFile(multipartFile)) {
                BufferedImage originalImage = ImageUtil.adjustImageDirection(multipartFile);
                ImageIO.write(originalImage, Objects.requireNonNull(extension), file);
                if (compressibleImageExtensions.contains(extension.toLowerCase())) {
                    ImageUtil.compressImage(savePath, imageQuality);
                }
            } else {
                multipartFile.transferTo(file);
            }
        } catch (Exception e) {
            throw new IOException("이미지의 뱡향을 조정하는 데 오류가 발생했습니다.", e);
        }

        FileUtil.setFilePermissions(file, savePath);
        return savePath;
    }

    public void deleteFile(String savedPath) {
        File fileToDelete = new File(savedPath);
        boolean deleted = fileToDelete.delete();
        if (!deleted) {
            log.error("Failed to delete file: {}", savedPath);
        }
    }
}
