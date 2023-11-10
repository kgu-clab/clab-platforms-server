package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Board;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResonseDto {

    private Long id;

    private String memberId;

    private String memberName;

    private String memberImageUrl;

    private String category;

    private String title;

    private String content;

    private LocalDateTime updateTime;

    private LocalDateTime createdAt;

    public static BoardResonseDto of(Board board) {
        BoardResonseDto boardResonseDto =  ModelMapperUtil.getModelMapper().map(board, BoardResonseDto.class);
        boardResonseDto.setMemberId(board.getMember().getId());
        boardResonseDto.setMemberName(board.getMember().getName());
        boardResonseDto.setMemberImageUrl(board.getMember().getImageUrl());
        boardResonseDto.setUpdateTime(board.getUpdateTime());
        boardResonseDto.setCreatedAt(board.getCreatedAt());
        return boardResonseDto;
    }

}
