package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Recruitment;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitmentResponseDto {

    private Long id;

    private String period;

    private ApplicationType applicationType;

    private String target;

    private String status;

    private LocalDateTime updatedAt;

    public static RecruitmentResponseDto of(Recruitment recruitment) {
        return ModelMapperUtil.getModelMapper().map(recruitment, RecruitmentResponseDto.class);
    }

}
