package page.clab.api.domain.community.board.application.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardHashtagRequestDto {

    private Long boardId;
    private List<String> hashtagNameList;
}
