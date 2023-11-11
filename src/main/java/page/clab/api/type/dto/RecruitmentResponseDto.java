package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Recruitment;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

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

    private LocalDateTime updateTime;

    public static RecruitmentResponseDto of(Recruitment recruitment) {
        return ModelMapperUtil.getModelMapper().map(recruitment, RecruitmentResponseDto.class);
    }

}
