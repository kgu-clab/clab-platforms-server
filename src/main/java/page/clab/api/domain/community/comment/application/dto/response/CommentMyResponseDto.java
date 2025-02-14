package page.clab.api.domain.community.comment.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentMyResponseDto {

    private Long id;
    private Long boardId;
    private String boardCategory;
    private String writer;
    private String writerImageUrl;
    private String content;
    private Long likes;
    private boolean hasLikeByMe;
    private LocalDateTime createdAt;
}
