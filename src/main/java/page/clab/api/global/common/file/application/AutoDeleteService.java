package page.clab.api.global.common.file.application;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import page.clab.api.global.common.file.dao.UploadFileRepository;
import page.clab.api.global.common.file.domain.UploadedFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoDeleteService {

    private final UploadFileRepository uploadFileRepository;

    @Value("${resource.file.path}")
    private String filePath;

    @Scheduled(cron = "0 0 0 * * *")
    public void autoDeleteFiles() {
        List<String> categoryPaths = Arrays.asList(
                "boards", "news", "books", "profiles",
                "activity-photos", "members", "forms", "attendance"
        );
        categoryPaths.stream()
                .map(category -> filePath + File.separator + category)
                .forEach(this::deleteFilesInDirectory);
    }

    private void deleteFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            log.info("No Directory : " + directory);
            return;
        }
        processFilesInDirectory(directory);
    }

    private void processFilesInDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            log.info("No file in Directory : " + directory);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                processFilesInDirectory(file);
            } else {
                log.info(file.getAbsolutePath() + " file found");
                processFile(file);
            }
        }
    }

    private void processFile(File file) {
        LocalDateTime currentDate = LocalDateTime.now();
        UploadedFile uploadedFile = uploadFileRepository.findBySavedPath(file.getAbsolutePath());
        if (uploadedFile == null) {
            log.info("No UploadedFile in DB : " + file.getAbsolutePath());
            return;
        }
        LocalDateTime fileCreatedAt = uploadedFile.getCreatedAt();
        long storagePeriod = uploadedFile.getStoragePeriod();
        if (fileCreatedAt.plusDays(storagePeriod).isBefore(currentDate)) {
            boolean deleted = file.delete();
            if (!deleted) {
                log.info("File Delete Error : " + file.getAbsolutePath());
            }
        }
    }

}
