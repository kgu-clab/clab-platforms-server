package page.clab.api.domain.activityGroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.Absent;
import page.clab.api.domain.activityGroup.domain.ApplyForm;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyFormResponseDto {

    private String applierName;

    private String applierId;

    private String applierDepartment;

    private String applierYear;

    private String applyReason;

    public static ApplyFormResponseDto of(ApplyForm applyForm) {
        return ApplyFormResponseDto.builder()
                .applierName(applyForm.getMember().getName())
                .applierId(applyForm.getMember().getId())
                .applierDepartment(applyForm.getMember().getDepartment())
                .applierYear(applyForm.getMember().getGrade().toString())
                .applyReason(applyForm.getApplyReason())
                .build();
    }

}
