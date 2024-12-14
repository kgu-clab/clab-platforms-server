package page.clab.api.domain.community.board.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardHashtagResponseDto {

    private Long id;
    private Long boardId;
    private Long hashtagId;
    private String name;
}
