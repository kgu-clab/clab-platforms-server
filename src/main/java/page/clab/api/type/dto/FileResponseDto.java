package page.clab.api.type.dto;

import lombok.*;
import page.clab.api.type.entity.UploadedFile;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponseDto {
    private Long id;

    private String uploaderId;

    private String uploaderName;

    private String originalFileName;

    private String saveFileName;

    private String savePath;

    private Long fileSize;

    private String contentType;

    private String category;

    public static FileResponseDto of(UploadedFile uploadedFile) {
        FileResponseDto fileResponseDto = ModelMapperUtil.getModelMapper().map(uploadedFile, FileResponseDto.class);
        if(uploadedFile.getUploader() != null){
            fileResponseDto.setUploaderId(uploadedFile.getUploader().getId());
            fileResponseDto.setUploaderName(uploadedFile.getUploader().getName());
        }
        return fileResponseDto;
    }

}
