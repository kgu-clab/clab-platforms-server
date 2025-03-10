package page.clab.api.domain.memberManagement.executive.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExecutiveResponseDto {

    private String executiveId;
    private String name;
    private String email;
    private String interests;
    private String position;
    private String imageUrl;
}
