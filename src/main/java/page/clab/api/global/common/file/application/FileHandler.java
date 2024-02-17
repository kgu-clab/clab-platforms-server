package page.clab.api.global.common.file.application;

import com.google.common.base.Strings;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.exception.FileUploadFailException;
import page.clab.api.global.util.ImageCompressionUtil;

@Component
@Slf4j
@Configuration
public class FileHandler {

    @Value("${resource.file.path}")
    private String filePath;

    private final Set<String> disallowExtensions = new HashSet<>();

    private final Set<String> imageExtensions = new HashSet<>();

    public FileHandler(
            @Value("${resource.file.disallow-extension}") String[] disallowExtensions,
            @Value("${resource.file.image-extension}") String[] imageExtensions
    ) {
        Arrays.stream(disallowExtensions).forEach(this.disallowExtensions::add);
        Arrays.stream(imageExtensions).forEach(this.imageExtensions::add);
    }

    public void init() {
        filePath = filePath.replace("/", File.separator).replace("\\", File.separator);
    }

    public String saveQRCodeImage(byte[] image, String category, String originalFilename, String extension, UploadedFile uploadedFile) throws IOException {
        init();

        fileValidation(originalFilename, extension);
        String saveFilename = makeSaveFileName(extension);
        String savePath = filePath + File.separator + category + File.separator + saveFilename;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        File file = new File(savePath);
        checkDir(file);
        ImageIO.write(bufferedImage, extension, file);
        save(file, savePath, extension);
        uploadedFile.setFileSize(file.length());
        uploadedFile.setSavedPath(savePath);
        uploadedFile.setSaveFileName(saveFilename);
        uploadedFile.setCategory(category);
        return "/" + saveFilename;
    }

    public String saveFile(MultipartFile multipartFile, String category, UploadedFile uploadedFile) throws IOException {
        init();

        String originalFilename = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        fileValidation(originalFilename, extension);
        String saveFilename = makeSaveFileName(extension);
        String savePath = filePath + File.separator + category + File.separator + saveFilename;

        File file = new File(savePath);
        checkDir(file);
        multipartFile.transferTo(file);
        save(file, savePath, extension);

        uploadedFile.setSavedPath(savePath);
        uploadedFile.setSaveFileName(saveFilename);
        uploadedFile.setCategory(category);
        return "/" + saveFilename;
    }
    private void fileValidation(String originalFilename, String extension) throws FileUploadFailException {
        if (!validateFilename(originalFilename)) {
            throw new FileUploadFailException("허용되지 않은 파일명 : " + originalFilename);
        }

        if (!validateExtension(extension)) {
            throw new FileUploadFailException("허용되지 않은 확장자 : " + originalFilename);
        }
    }

    private boolean validateExtension(String extension) {
        return !disallowExtensions.contains(extension.toLowerCase());
    }

    private boolean validateFilename(String fileName) {
        return !Strings.isNullOrEmpty(fileName);
    }

    private String makeSaveFileName(String extension){
        return (System.nanoTime() + "_" + UUID.randomUUID() + "." + extension);
    }

    private void checkDir(File file){
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    private void save(File file, String savePath, String extension) throws FileUploadFailException {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (imageExtensions.contains(extension.toLowerCase())) {
                ImageCompressionUtil.compressImage(savePath, 0.5f);
            }
            if (os.contains("win")) {
                file.setReadable(true);
                file.setWritable(false);
                file.setExecutable(false);
            } else {
                Runtime.getRuntime().exec("chmod 400 " + savePath);
            }
        } catch (IOException e) {
            throw new FileUploadFailException("파일 저장 실패", e);
        }
    }

}
