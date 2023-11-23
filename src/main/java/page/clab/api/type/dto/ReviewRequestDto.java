package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class ReviewRequestDto {

    @NotNull(message = "{notNull.review.activityGroupId}")
    @Schema(description = "활동 그룹 ID", example = "1", required = true)
    private Long activityGroupId;

    @NotNull(message = "{notNull.review.content}")
    @Size(min = 1, max = 1000, message = "{size.review.content}")
    @Schema(description = "후기", example = "C-Lab에는 다양한 분야에 대한 열정있는 사람들이 모이기 때문에 다양하고 활동적인 스터디 그룹과 프로젝트 팀이 있습니다. 신입생이라도 자유롭게 스터디 그룹을 만들고 사람들을 모아서 원하는 분야와 관련된 기술을 알아보고 같이 공부하기 좋습니다!", required = true)
    private String content;

}
