package page.clab.api.domain.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardMyResponseDto {

    private Long id;

    private String category;

    private String writerName;

    private String title;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static BoardMyResponseDto toDto(Board board, Member member) {
        return BoardMyResponseDto.builder()
                .id(board.getId())
                .category(board.getCategory().getKey())
                .writerName(board.isWantAnonymous() ? board.getNickname() : member.getName())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .createdAt(board.getCreatedAt())
                .build();
    }

}
