package page.clab.api.global.common.file.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.global.common.file.domain.UploadedFile;

public interface UploadFileRepository extends JpaRepository<UploadedFile, Long> {

    UploadedFile findBySavedPath(String savedPath);

    Optional<UploadedFile> findByUrl(String url);

    UploadedFile findByCategoryAndOriginalFileName(String category, String originalFileName);

}
