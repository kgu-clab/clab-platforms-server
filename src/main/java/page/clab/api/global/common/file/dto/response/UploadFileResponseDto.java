package page.clab.api.global.common.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadFileResponseDto {

    private Long id;

    private String uploaderId;

    private String uploaderName;

    private String originalFileName;

    private String saveFileName;

    private String savePath;

    private Long fileSize;

    private String contentType;

    private String category;

    public static UploadFileResponseDto of(UploadedFile uploadedFile) {
        UploadFileResponseDto uploadFileResponseDto = ModelMapperUtil.getModelMapper().map(uploadedFile, UploadFileResponseDto.class);
        if (uploadedFile.getUploader() != null) {
            uploadFileResponseDto.setUploaderId(uploadedFile.getUploader().getId());
            uploadFileResponseDto.setUploaderName(uploadedFile.getUploader().getName());
        }
        return uploadFileResponseDto;
    }

}
