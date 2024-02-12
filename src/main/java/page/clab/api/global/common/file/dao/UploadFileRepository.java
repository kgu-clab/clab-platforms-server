package page.clab.api.global.common.file.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.global.common.file.domain.UploadedFile;

public interface UploadFileRepository extends JpaRepository<UploadedFile, Long> {

    UploadedFile findBySavedPath(String savedPath);

    UploadedFile findByUrl(String url);

}
