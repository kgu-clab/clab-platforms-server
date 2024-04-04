package page.clab.api.global.common.file.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class UploadedFileResponseDto {

    private String fileUrl;

    private String originalFileName;

    private Long storagePeriod;

    private LocalDateTime createdAt;

    public static List<UploadedFileResponseDto> toDto(List<UploadedFile> uploadedFiles) {
        return uploadedFiles.stream()
                .map(UploadedFileResponseDto::toDto)
                .toList();
    }

    public static UploadedFileResponseDto toDto(UploadedFile uploadedFile) {
        return UploadedFileResponseDto.builder()
                .fileUrl(uploadedFile.getUrl())
                .originalFileName(uploadedFile.getOriginalFileName())
                .storagePeriod(uploadedFile.getStoragePeriod())
                .createdAt(uploadedFile.getCreatedAt())
                .build();
    }

}
