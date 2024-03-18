package page.clab.api.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentGetAllResponseDto {

    private Long id;

    private String writer;

    private String writerImageUrl;

    private Long writerRoleLevel;

    private String content;

    private List<CommentGetAllResponseDto> children;

    private Long likes;

    private boolean hasLikeByMe;

    @JsonProperty("isOwner")
    private boolean isOwner;

    private LocalDateTime createdAt;

    public static CommentGetAllResponseDto of(Comment comment, String currentMemberId) {
        CommentGetAllResponseDto commentGetAllResponseDto = ModelMapperUtil.getModelMapper().map(comment, CommentGetAllResponseDto.class);

        commentGetAllResponseDto.setWriterRoleLevel(comment.getWriter().getRole().toLong());

        if(comment.isWantAnonymous()){
            commentGetAllResponseDto.setWriter(comment.getNickname());
            commentGetAllResponseDto.setWriterImageUrl(null);
        }
        else{
            commentGetAllResponseDto.setWriter(comment.getWriter().getName());
            commentGetAllResponseDto.setWriterImageUrl(comment.getWriter().getImageUrl());
        }

        commentGetAllResponseDto.setOwner(comment.getWriter().getId().equals(currentMemberId));

        List<CommentGetAllResponseDto> childrenDto = comment.getChildren().stream()
                .map(child -> CommentGetAllResponseDto.of(child, currentMemberId))
                .collect(Collectors.toList());
        commentGetAllResponseDto.setChildren(childrenDto);
        return commentGetAllResponseDto;
    }

}
