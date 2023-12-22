package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.FileEntity;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findBySavedPath(String savedPath);

    FileEntity findBySaveFileName(String saveFileName);
    List<FileEntity> findByCategory(String category);
}
