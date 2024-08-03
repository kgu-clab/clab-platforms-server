package page.clab.api.domain.activity.review.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateRequestDto {

    @Schema(description = "후기", example = "C-Lab에는 다양한 분야에 대한 열정있는 사람들이 모이기 때문에 다양하고 활동적인 스터디 그룹과 프로젝트 팀이 있습니다. 신입생이라도 자유롭게 스터디 그룹을 만들고 사람들을 모아서 원하는 분야와 관련된 기술을 알아보고 같이 공부하기 좋습니다!")
    private String content;

    @Schema(description = "공개 여부", example = "false")
    private Boolean isPublic;
}
