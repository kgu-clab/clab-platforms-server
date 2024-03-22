package page.clab.api.domain.activityGroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ApplyForm;
import page.clab.api.domain.member.domain.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyFormRequestDto {

    @NotNull(message = "{notnull.applyForm.applyReason}")
    @Schema(description = "지원 동기", example = "백엔드에 관심이 있어서")
    private String applyReason;

    public static ApplyForm toEntity(ApplyFormRequestDto requestDto, ActivityGroup activityGroup, Member member) {
        return ApplyForm.builder()
                .activityGroup(activityGroup)
                .member(member)
                .applyReason(requestDto.getApplyReason())
                .build();
    }

}
