package page.clab.api.domain.members.support.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.members.support.domain.SupportCategory;

@Getter
@Setter
public class SupportUpdateRequestDTO {

    @Schema(description = "제목", example = "이미지 저장이 이상하게 되는것 같습니다.")
    private String title;

    @Schema(description = "내용", example = "블로그 글에 들어가면 정상적으로 뜨지만......")
    private String content;

    @Schema(description = "이미지 URL", example = "https://www.clab.page/assets/logoWhite-fc1ef9a0.webp")
    private String imageUrl;

    @Schema(description = "카테고리", example = "BUG")
    private SupportCategory category;
}
