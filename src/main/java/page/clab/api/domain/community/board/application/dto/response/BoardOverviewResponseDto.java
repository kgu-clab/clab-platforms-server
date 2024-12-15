package page.clab.api.domain.community.board.application.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardOverviewResponseDto {

    private Long id;
    private String category;
    private String writerId;
    private String writerName;
    private String title;
    private Long commentCount;
    private String imageUrl;
    private LocalDateTime createdAt;
    private List<BoardHashtagResponseDto> boardHashtagInfos;
}
