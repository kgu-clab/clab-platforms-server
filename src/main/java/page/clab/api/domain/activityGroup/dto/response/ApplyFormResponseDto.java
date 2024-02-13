package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ApplyForm;

import java.time.LocalDateTime;

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

    private String applierContact;

    private String applyReason;

    private LocalDateTime createdAt;

    public static ApplyFormResponseDto of(ApplyForm applyForm) {
        return ApplyFormResponseDto.builder()
                .applierName(applyForm.getMember().getName())
                .applierId(applyForm.getMember().getId())
                .applierDepartment(applyForm.getMember().getDepartment())
                .applierYear(applyForm.getMember().getGrade().toString())
                .applyReason(applyForm.getApplyReason())
                .applierContact(applyForm.getMember().getContact())
                .build();
    }

}
