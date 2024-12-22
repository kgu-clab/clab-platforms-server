package page.clab.api.domain.community.board.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardMyResponseDto {

    private Long id;
    private String category;
    private String writerName;
    private String title;
    private String imageUrl;
    private List<BoardHashtagResponseDto> boardHashtagInfos;
    private LocalDateTime createdAt;
}
