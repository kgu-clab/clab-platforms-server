package page.clab.api.global.common.file.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.global.common.file.dao.UploadFileRepository;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

/**
 * {@code UploadedFileService}는 업로드된 파일을 저장, 조회, 및 유효성 검사를 수행하는 서비스입니다.
 *
 * <p>이 서비스는 파일의 URL을 기반으로 파일을 조회하거나, 특정 카테고리에 속하는 최신 파일을 조회하며,
 * 여러 URL 목록을 받아 해당하는 파일들을 검색하는 등의 기능을 제공합니다.</p>
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>{@link #saveUploadedFile(UploadedFile)} - 파일 정보를 데이터베이스에 저장합니다.</li>
 *     <li>{@link #getUploadedFileByUrl(String)} - URL을 기반으로 파일을 조회합니다.</li>
 *     <li>{@link #getUniqueUploadedFileByCategory(String)} - 특정 카테고리의 최신 파일을 조회합니다.</li>
 *     <li>{@link #getUploadedFilesByUrls(List)} - URL 목록에 해당하는 파일들을 조회하며, 누락된 파일이 있을 경우 예외를 발생시킵니다.</li>
 * </ul>
 *
 * <p>이 서비스는 파일 조회와 관련된 기본적인 예외 처리도 포함하고 있으며, 요청된 파일이 존재하지 않을 경우
 * {@link BaseException} - {@code ErrorCode.NOT_FOUNT}을 발생시킵니다.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadedFileService {

    private final UploadFileRepository uploadFileRepository;

    public UploadedFile saveUploadedFile(UploadedFile uploadedFile) {
        return uploadFileRepository.save(uploadedFile);
    }

    public UploadedFile getUploadedFileByUrl(String url) {
        return uploadFileRepository.findByUrl(url)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND, "파일을 찾을 수 없습니다."));
    }

    public UploadedFile getUniqueUploadedFileByCategory(String category) {
        return uploadFileRepository.findTopByCategoryOrderByCreatedAtDesc(category);
    }

    public List<UploadedFile> getUploadedFilesByUrls(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) {
            return new ArrayList<>();
        }
        List<UploadedFile> uploadedFiles = uploadFileRepository.findAllByUrlIn(fileUrls);
        if (uploadedFiles.size() != fileUrls.size()) {
            throw new BaseException(ErrorCode.NOT_FOUND, "서버에 업로드되지 않은 파일이 포함되어 있습니다.");
        }
        return uploadedFiles;
    }
}
