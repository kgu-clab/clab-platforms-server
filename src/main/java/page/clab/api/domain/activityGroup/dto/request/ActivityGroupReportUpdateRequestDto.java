package page.clab.api.domain.activityGroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ActivityGroupReportUpdateRequestDto {

    @Schema(description = "차시", example = "1")
    private Long turn;

    @Schema(description = "제목", example = "C언어 기초 1차시 보고서")
    private String title;

    @Schema(description = "내용", example = "변수, 자료형에 대해 공부")
    private String content;

}
