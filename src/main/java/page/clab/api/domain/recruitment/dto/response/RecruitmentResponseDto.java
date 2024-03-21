package page.clab.api.domain.recruitment.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitmentResponseDto {

    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private ApplicationType applicationType;

    private String target;

    private String status;

    private LocalDateTime updatedAt;

    public static RecruitmentResponseDto of(Recruitment recruitment) {
        return ModelMapperUtil.getModelMapper().map(recruitment, RecruitmentResponseDto.class);
    }

}
