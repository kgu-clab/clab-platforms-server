package page.clab.api.domain.memberManagement.executive.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutiveRequestDto {

    @NotNull(message = "{notNull.executive.executiveId}")
    @Schema(description = "학번", example = "202310000")
    private String executiveId;

    @NotNull(message = "{notNull.executive.name}")
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotNull(message = "{notNull.executive.email}")
    @Schema(description = "이메일", example = "clab.coreteam@gmail.com")
    private String email;

    @NotNull(message = "{notNull.executive.interests}")
    @Schema(description = "분야", example = "Back-End")
    private String interests;

    @Schema(description = "프로필 이미지", example = "https://www.clab.page/assets/dongmin-860f3a1e.jpeg")
    private String imageUrl;
}
