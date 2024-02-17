package page.clab.api.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDetailsResponseDto {

    private Long id;

    private String writer;

    private String memberImageUrl;

    private String title;

    private String content;

    private List<UploadedFileResponseDto> files = new ArrayList<>();

    private Long likes;

    private boolean hasLikeByMe;

    @JsonProperty("isOwner")
    private boolean isOwner;

    private LocalDateTime createdAt;

    public static BoardDetailsResponseDto of(Board board) {
        BoardDetailsResponseDto boardResponseDto = ModelMapperUtil.getModelMapper().map(board, BoardDetailsResponseDto.class);

        if(board.isWantAnonymous()){
            boardResponseDto.setWriter(board.getNickName());
            boardResponseDto.setMemberImageUrl(null);
        }
        else{
            boardResponseDto.setWriter(board.getMember().getName());
            boardResponseDto.setMemberImageUrl(board.getMember().getImageUrl());
        }

        return boardResponseDto;
    }

}
