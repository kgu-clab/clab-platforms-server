package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Board;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDetailsResponseDto {

    private Long id;

    private String memberName;

    private String memberImageUrl;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    public static BoardDetailsResponseDto of(Board board) {
        BoardDetailsResponseDto boardResponseDto =  ModelMapperUtil.getModelMapper().map(board, BoardDetailsResponseDto.class);
        boardResponseDto.setMemberName(board.getMember().getName());
        boardResponseDto.setMemberImageUrl(board.getMember().getImageUrl());
        return boardResponseDto;
    }

}
