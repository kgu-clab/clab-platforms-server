package page.clab.api.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

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

    public static BoardDetailsResponseDto toDto(Board board, boolean hasLikeByMe, boolean isOwner) {
        return BoardDetailsResponseDto.builder()
                .id(board.getId())
                .writer(board.isWantAnonymous() ? board.getNickname() : board.getMember().getName())
                .writerRoleLevel(board.getMember().getRole().toRoleLevel())
                .memberImageUrl(board.isWantAnonymous() ? null : board.getMember().getImageUrl())
                .title(board.getTitle())
                .content(board.getContent())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .likes(board.getLikes())
                .hasLikeByMe(hasLikeByMe)
                .isOwner(isOwner)
                .createdAt(board.getCreatedAt())
                .build();
    }

}
