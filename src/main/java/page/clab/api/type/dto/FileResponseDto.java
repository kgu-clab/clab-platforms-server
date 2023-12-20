package page.clab.api.type.dto;

import lombok.*;
import page.clab.api.type.entity.File;
import page.clab.api.type.entity.Member;
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

    public static FileResponseDto of(File file) {
        FileResponseDto fileResponseDto = ModelMapperUtil.getModelMapper().map(file, FileResponseDto.class);
        if(file.getUploader() != null){
            fileResponseDto.setUploaderId(file.getUploader().getId());
            fileResponseDto.setUploaderName(file.getUploader().getName());
        }
        return fileResponseDto;
    }

}
