package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.UploadedFile;

public interface FileRepository extends JpaRepository<UploadedFile, Long> {
    UploadedFile findBySavedPath(String savedPath);
    UploadedFile findBySaveFileName(String saveFileName);
}
