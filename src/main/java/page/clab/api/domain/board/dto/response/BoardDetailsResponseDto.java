package page.clab.api.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDetailsResponseDto {

    private Long id;

    private String writer;

    private Long writerRoleLevel;

    private String memberImageUrl;

    private String title;

    private String content;

    private List<UploadedFileResponseDto> files = new ArrayList<>();

    private Long likes;

    private boolean hasLikeByMe;

    @JsonProperty("isOwner")
    private Boolean isOwner;

    private LocalDateTime createdAt;

    public static BoardDetailsResponseDto create(Board board, boolean hasLikeByMe, boolean isOwner) {
        BoardDetailsResponseDto boardResponseDto = ModelMapperUtil.getModelMapper().map(board, BoardDetailsResponseDto.class);
        if (board.isWantAnonymous()) {
            boardResponseDto.setWriter(board.getNickname());
            boardResponseDto.setMemberImageUrl(null);
        } else {
            boardResponseDto.setWriter(board.getMember().getName());
            boardResponseDto.setMemberImageUrl(board.getMember().getImageUrl());
        }
        boardResponseDto.setWriterRoleLevel(board.getMember().getRole().toLong());
        boardResponseDto.setHasLikeByMe(hasLikeByMe);
        boardResponseDto.setIsOwner(isOwner);
        return boardResponseDto;
    }

}
