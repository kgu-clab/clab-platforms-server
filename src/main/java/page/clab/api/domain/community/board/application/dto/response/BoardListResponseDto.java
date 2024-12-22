package page.clab.api.domain.community.board.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardListResponseDto {

    private Long id;
    private String writerId;
    private String writerName;
    private String category;
    private String title;
    private String content;
    private Long commentCount;
    private String imageUrl;
    private List<BoardHashtagResponseDto> boardHashtagInfos;
    private LocalDateTime createdAt;
}
