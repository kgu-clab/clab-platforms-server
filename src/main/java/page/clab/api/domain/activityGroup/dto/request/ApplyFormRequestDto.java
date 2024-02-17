package page.clab.api.domain.activityGroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyFormRequestDto {

    @NotNull(message = "{notnull.applyForm.applierName}")
    @Schema(description = "지원자 이름", example = "홍길동")
    private String applierName;

    @NotNull(message = "{notnull.applyForm.applierId}")
    @Schema(description = "지원자 학번", example = "202311011")
    private String applierId;

    @NotNull(message = "{notnull.applyForm.applierDepartment}")
    @Schema(description = "지원자 학과", example = "컴퓨터공학전공")
    private String applierDepartment;

    @NotNull(message = "{notnull.applyForm.applierYear}")
    @Schema(description = "지원자 학년", example = "3")
    private String applierYear;

    @NotNull(message = "{notnull.applyForm.applyReason}")
    @Schema(description = "지원 동기", example = "백엔드에 관심이 있어서")
    private String applyReason;

}
