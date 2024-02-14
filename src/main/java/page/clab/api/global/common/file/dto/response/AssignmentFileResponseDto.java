package page.clab.api.global.common.file.dto.response;

import java.time.LocalDateTime;
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
public class AssignmentFileResponseDto {

    private String fileUrl;

    private String originalFileName;

    private LocalDateTime storageDateTimeOfFile;

    public static AssignmentFileResponseDto of(UploadedFile uploadedFile) {
        return ModelMapperUtil.getModelMapper().map(uploadedFile, AssignmentFileResponseDto.class);
    }

}
