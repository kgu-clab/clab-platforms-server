package page.clab.api.domain.members.support.application.dto.shared;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupportAnswerInfoDto {
    private final Long supportId;
    private final String memberId;
    private final String title;
}
