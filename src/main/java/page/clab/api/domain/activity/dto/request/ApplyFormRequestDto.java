package page.clab.api.domain.activity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.ApplyForm;
import page.clab.api.domain.member.domain.Member;

@Getter
@Setter
public class ApplyFormRequestDto {

    @NotNull(message = "{notnull.applyForm.applyReason}")
    @Schema(description = "지원 동기", example = "백엔드에 관심이 있어서")
    private String applyReason;

    public static ApplyForm toEntity(ApplyFormRequestDto requestDto, ActivityGroup activityGroup, Member member) {
        return ApplyForm.builder()
                .activityGroup(activityGroup)
                .memberId(member.getId())
                .applyReason(requestDto.getApplyReason())
                .build();
    }

}
