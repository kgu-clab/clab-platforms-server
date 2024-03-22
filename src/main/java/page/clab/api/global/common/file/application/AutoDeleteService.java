package page.clab.api.global.common.file.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import page.clab.api.global.common.file.dao.UploadFileRepository;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoDeleteService {

    private final UploadFileRepository uploadFileRepository;

    @Value("${resource.file.path}")
    private String filePath;

    @Scheduled(cron = "0 0 0 * * *")
    public void autoDeleteExpiredFiles() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<String> categoryPaths = Arrays.asList(
                "boards", "news", "books", "profiles",
                "activity-photos", "members", "forms", "attendance"
        );

        categoryPaths.stream()
                .map(category -> filePath + File.separator + category)
                .forEach(directoryPath -> deleteExpiredFilesInDirectory(new File(directoryPath), currentDate));
    }

    private void deleteExpiredFilesInDirectory(File directory, LocalDateTime currentDate) {
        if (!directory.exists()) {
            log.info("Directory does not exist: {}", directory);
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            log.info("No files in directory: {}", directory);
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                deleteExpiredFilesInDirectory(file, currentDate);
            } else {
                checkAndDeleteFileIfExpired(file, currentDate);
            }
        }
    }

    private void checkAndDeleteFileIfExpired(File file, LocalDateTime currentDate) {
        UploadedFile uploadedFile = uploadFileRepository.findBySavedPath(file.getAbsolutePath());
        if (uploadedFile == null) {
            log.info("No matching UploadedFile record in DB for file: {}", file.getAbsolutePath());
            return;
        }

        LocalDateTime expirationDate = uploadedFile.getCreatedAt().plusDays(uploadedFile.getStoragePeriod());
        if (currentDate.isAfter(expirationDate)) {
            boolean deleted = file.delete();
            if (deleted) {
                log.info("Deleted expired file: {}", file.getAbsolutePath());
            } else {
                log.error("Failed to delete expired file: {}", file.getAbsolutePath());
            }
        }
    }
}
