package page.clab.api.domain.community.hashtag.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HashtagResponseDto {

    private Long id;
    private String name;
    private Long boardUsageCount;
}
