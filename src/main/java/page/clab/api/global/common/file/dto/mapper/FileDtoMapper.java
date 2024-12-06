package page.clab.api.global.common.file.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.io.File;
import java.util.Date;
import java.util.List;

@Component
public class FileDtoMapper {

    public List<UploadedFileResponseDto> toDto(List<UploadedFile> uploadedFiles) {
        return uploadedFiles.stream()
                .map(this::toDto)
                .toList();
    }

    public UploadedFileResponseDto toDto(UploadedFile uploadedFile) {
        return UploadedFileResponseDto.builder()
                .fileUrl(uploadedFile.getUrl())
                .originalFileName(uploadedFile.getOriginalFileName())
                .storagePeriod(uploadedFile.getStoragePeriod())
                .createdAt(uploadedFile.getCreatedAt())
                .build();
    }

    public DeleteFileRequestDto of(String url) {
        return DeleteFileRequestDto.builder()
                .url(url)
                .build();
    }

    public FileInfo create(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }

        return FileInfo.builder()
                .fileName(file.getName())
                .fileSizeInBytes(file.length())
                .creationDate(new Date(file.lastModified()))
                .modificationDate(new Date(file.lastModified()))
                .build();
    }
}
