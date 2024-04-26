package page.clab.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.RandomNicknameUtil;

@Getter
@Setter
public class CommentRequestDto {

    @NotNull(message = "{notNull.comment.content}")
    @Schema(description = "내용", example = "댓글 내용", required = true)
    private String content;

    @NotNull(message = "{notNull.comment.wantAnonymous}")
    @Schema(description = "익명 사용 여부", example = "false", required = true)
    private boolean wantAnonymous;

    public static Comment toEntity(CommentRequestDto requestDto, Board board, Member member, Comment parent) {
        return Comment.builder()
                .board(board)
                .writer(member)
                .nickname(RandomNicknameUtil.makeRandomNickname())
                .content(requestDto.getContent())
                .parent(parent)
                .wantAnonymous(requestDto.isWantAnonymous())
                .likes(0L)
                .build();
    }

}
