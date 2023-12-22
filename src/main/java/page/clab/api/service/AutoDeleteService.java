package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import page.clab.api.repository.FileRepository;
import page.clab.api.type.entity.FileEntity;
import java.io.File;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AutoDeleteService {

    private final FileRepository fileRepository;

    @Value("${resource.file.url}")
    private String fileURL;

    @Value("${resource.file.path}")
    private String filePath;

    @Scheduled(cron = "20 * * * * *"/*"0 0 0 * * *"*/) //20 * * * * * : 20초마다. test용
    public void autoDeleteFiles(){
        String boardCategoryPath = filePath + File.separator +"boards";
        deleteFilesInDirectory(boardCategoryPath);

        String newsCategoryPath = filePath + File.separator + "news";
        deleteFilesInDirectory(newsCategoryPath);

        String bookCategoryPath = filePath + File.separator + "books";
        deleteFilesInDirectory(bookCategoryPath);

        String profilesCategoryPath = filePath + File.separator + "profiles";
        deleteFilesInDirectory(profilesCategoryPath);

        String activityCategoryPath = filePath + File.separator + "activity-photos";
        deleteFilesInDirectory(activityCategoryPath);

        String membersCategoryPath = filePath + File.separator + "members";
        deleteFilesInDirectory(membersCategoryPath);

        String formsCategoryPath = filePath + File.separator + "forms";
        deleteFilesInDirectory(formsCategoryPath);
    }

    private void deleteFilesInDirectory(String directoryPath){

        Logger logger = LoggerFactory.getLogger(this.getClass());

        File directory = new File(directoryPath); //${user.dir}/could/boards

        if(!directory.exists()){
            logger.info ("No Directory." + directoryPath);
            return;
        }

        processFilesInDirectory(directory);
    }

    private void processFilesInDirectory(File directory) {

        Logger logger = LoggerFactory.getLogger(this.getClass());

        File[] files = directory.listFiles();

        if(files == null){
            logger.info("No file in Directory : " + directory);
        }

        for (File file : files) {
            if (file.isDirectory()) {// 디렉토리인 경우 재귀 호출
                processFilesInDirectory(file);
            } else {// 파일인 경우 작업 수행
                logger.info(file.getAbsolutePath() + "file found");
                processFile(file);
            }
        }
    }

    private void processFile(File file) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        LocalDateTime currentDate = LocalDateTime.now();

        FileEntity fileEntity = fileRepository.findBySavedPath(file.getAbsolutePath());
        if(fileEntity == null){
            logger.info("No Entity : " + file.getAbsolutePath());
            return;
        }

        LocalDateTime fileCreatedAt  = fileEntity.getCreatedAt();

        long storagePeriod = fileEntity.getStoragePeriod();

        //파일생성날짜 + 보관기간이 현재날짜보다 이전이면 삭제
        if(fileCreatedAt.plusDays(storagePeriod).isBefore(currentDate)){
            fileRepository.deleteById(fileEntity.getId());
            boolean deleted = file.delete();
            if (deleted) {
                logger.info("Deleted file: " + file.getName());
            } else {
                logger.info("Failed to delete file: " + file.getName());
            }
        }
    }
}
