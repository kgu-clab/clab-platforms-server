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
public class RecruitmentRequestDto {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private ApplicationType applicationType;

    private String target;

    private String status;

    public static RecruitmentRequestDto of(Recruitment recruitment) {
        return ModelMapperUtil.getModelMapper().map(recruitment, RecruitmentRequestDto.class);
    }

}
