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

/**
 * {@code AutoDeleteService}는 지정된 경로의 파일을 자동으로 삭제하는 서비스입니다.
 *
 * <p>이 서비스는 매일 자정에 스케줄된 작업을 수행하며, 파일의 유효 기간이 만료되었거나
 * 데이터베이스에서 해당 파일의 정보가 존재하지 않는 경우 해당 파일을 삭제합니다.
 * 파일 삭제 작업은 다양한 카테고리에 대해 수행되며, 이를 통해 불필요한 파일을 자동으로 정리합니다.</p>
 *
 * 주요 기능:
 * <ul>
 *     <li>{@link #autoDeleteExpiredFiles()} - 스케줄에 따라 만료된 파일 또는 데이터베이스 정보가 없는 파일을 자동으로 삭제합니다.</li>
 *     <li>{@link #deleteUselessFilesInDirectory(File, LocalDateTime)} - 특정 디렉토리 내 파일을 순회하며 유효성 검사를 수행하고 필요 시 삭제합니다.</li>
 *     <li>{@link #checkAndDeleteFileIfExpired(File, LocalDateTime)} - 파일의 만료 여부를 확인하고 만료된 경우 삭제합니다.</li>
 *     <li>{@link #checkAndDeleteFileIfInformationDoesNotExistInDB(File)} - 데이터베이스에 파일 정보가 없는 경우 파일을 삭제합니다.</li>
 *     <li>{@link #deleteFile(File)} - 파일을 삭제하고 로그에 기록합니다.</li>
 * </ul>
 */
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
                .forEach(directoryPath -> deleteUselessFilesInDirectory(new File(directoryPath), currentDate));
    }

    private void deleteUselessFilesInDirectory(File directory, LocalDateTime currentDate) {
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
                deleteUselessFilesInDirectory(file, currentDate);
            } else {
                checkAndDeleteFileIfExpired(file, currentDate);
                checkAndDeleteFileIfInformationDoesNotExistInDB(file);
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
            deleteFile(file);
        }
    }

    private void checkAndDeleteFileIfInformationDoesNotExistInDB(File file) {
        UploadedFile uploadedFile = uploadFileRepository.findBySavedPath(file.getAbsolutePath());
        if (uploadedFile == null) {
            deleteFile(file);
        }
    }

    private void deleteFile(File file) {
        boolean deleted = file.delete();
        if (deleted) {
            log.info("Deleted unknown file: {}", file.getAbsolutePath());
        } else {
            log.error("Failed to delete unknown file: {}", file.getAbsolutePath());
        }
    }
}
