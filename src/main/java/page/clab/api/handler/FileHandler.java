package page.clab.api.handler;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.FileUploadFailException;
import page.clab.api.type.entity.UploadedFile;
import page.clab.api.util.ImageCompressionUtil;

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

    public String saveFile(MultipartFile multipartFile, String category, UploadedFile uploadedFile) throws FileUploadFailException {
        String originalFilename = multipartFile.getOriginalFilename();
        if (!validateFilename(originalFilename)) {
            throw new FileUploadFailException("허용되지 않은 파일명 : " + originalFilename);
        }
        String extension = FilenameUtils.getExtension(originalFilename);
        if (!validateExtension(extension)) {
            throw new FileUploadFailException("허용되지 않은 확장자 : " + originalFilename);
        }
        String saveFilename = category.startsWith("members") ? originalFilename :
                System.nanoTime() + "_" + UUID.randomUUID() + "." + extension;
        String savePath = filePath + File.separator + category + File.separator + saveFilename;
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            String os = System.getProperty("os.name").toLowerCase();
            multipartFile.transferTo(file);
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
        uploadedFile.setSavedPath(savePath);
        uploadedFile.setSaveFileName(saveFilename);
        uploadedFile.setCategory(category);
        return "/" + saveFilename;
    }

    private boolean validateExtension(String extension) {
        return !disallowExtensions.contains(extension.toLowerCase());
    }

    private boolean validateFilename(String fileName) {
        return !Strings.isNullOrEmpty(fileName);
    }

}
