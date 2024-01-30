package page.clab.api.domain.activityGroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ApplyForm;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyFormRequestDto {

    @NotNull(message = "{notnull.applyForm.activityGroupId}")
    @Schema(description = "그룹 아이디", example = "1")
    private Long activityGroupId;

    @NotNull(message = "{notnull.applyForm.applierId}")
    @Schema(description = "지원자 아이디", example = "202311011")
    private String applierId;

    @NotNull(message = "{notnull.applyForm.contact}")
    @Schema(description = "지원자 연락처", example = "01012345678")
    private String contact;

    @NotNull(message = "{notnull.applyForm.email}")
    @Schema(description = "지원자 이메일", example = "asdf@naver.com")
    private String email;

    @NotNull(message = "{notnull.applyForm.applyReason}")
    @Schema(description = "지원 동기", example = "백엔드에 관심이 있어서")
    private String applyReason;

    @NotNull(message = "{notnull.applyForm.spec}")
    @Schema(description = "지원자 스펙", example = "네트워크 관리사 2급 자격증")
    private String spec;

    public static ApplyFormRequestDto of(ApplyForm applyForm){
        return ApplyFormRequestDto.builder()
                .activityGroupId(applyForm.getActivityGroup().getId())
                .applierId(applyForm.getMember().getId())
                .applyReason(applyForm.getApplyReason())
                .spec(applyForm.getSpec())
                .contact(applyForm.getContact())
                .email(applyForm.getEmail())
                .build();
    }

}
