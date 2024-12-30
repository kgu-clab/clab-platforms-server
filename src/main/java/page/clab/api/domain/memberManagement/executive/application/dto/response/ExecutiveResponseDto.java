package page.clab.api.domain.memberManagement.executive.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExecutiveResponseDto {

    private String id;
    private String name;
    private String email;
    private String field;
    private String position;
    private String imageUrl;
}
