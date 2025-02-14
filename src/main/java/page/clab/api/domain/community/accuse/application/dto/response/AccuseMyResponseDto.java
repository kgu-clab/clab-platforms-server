package page.clab.api.domain.community.accuse.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.TargetType;

@Getter
@Builder
public class AccuseMyResponseDto {

    private TargetType targetType;
    private Long targetId;
    private String reason;
    private AccuseStatus accuseStatus;
    private LocalDateTime createdAt;
}
