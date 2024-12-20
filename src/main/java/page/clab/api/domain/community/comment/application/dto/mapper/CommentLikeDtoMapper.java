package page.clab.api.domain.community.comment.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.comment.application.dto.response.CommentLikeToggleResponseDto;

@Component
public class CommentLikeDtoMapper {

    public CommentLikeToggleResponseDto toDto(Long boardId, Long commentLikes, Boolean isDeleted) {
        return CommentLikeToggleResponseDto.builder()
                .boardId(boardId)
                .likes(commentLikes)
                .isDeleted(isDeleted)
                .build();
    }
}
