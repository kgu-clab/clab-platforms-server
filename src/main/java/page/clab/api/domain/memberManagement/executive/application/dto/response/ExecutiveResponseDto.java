package page.clab.api.domain.memberManagement.executive.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.executive.domain.ExecutivePosition;

@Getter
@Builder
public class ExecutiveResponseDto {

    private String id;
    private String name;
    private String email;
    private String field;
    private ExecutivePosition position;
    private String imageUrl;
}
