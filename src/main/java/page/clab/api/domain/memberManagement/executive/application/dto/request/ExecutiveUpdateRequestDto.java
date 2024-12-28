package page.clab.api.domain.memberManagement.executive.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.memberManagement.executive.domain.ExecutivePosition;

@Getter
@Setter
public class ExecutiveUpdateRequestDto {

    @Schema(description = "학번", example = "202310000")
    private String id;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "이메일", example = "clab.coreteam@gamil.com")
    private String email;

    @Schema(description = "분야", example = "Back-End")
    private String field;

    @Schema(description = "직급", example = "PRESIDENT")
    private ExecutivePosition position;

    @Schema(description = "프로필 이미지", example = "https://www.clab.page/assets/dongmin-860f3a1e.jpeg")
    private String imageUrl;
}
