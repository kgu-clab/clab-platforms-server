package page.clab.api.type.dto;

import lombok.*;
import page.clab.api.type.entity.FileEntity;
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

    public static FileResponseDto of(FileEntity fileEntity) {
        FileResponseDto fileResponseDto = ModelMapperUtil.getModelMapper().map(fileEntity, FileResponseDto.class);
        if(fileEntity.getUploader() != null){
            fileResponseDto.setUploaderId(fileEntity.getUploader().getId());
            fileResponseDto.setUploaderName(fileEntity.getUploader().getName());
        }
        return fileResponseDto;
    }

}
