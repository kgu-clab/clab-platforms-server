package page.clab.api.domain.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardListResponseDto {

    private Long id;

    private String writerId;

    private String writerName;

    private String category;

    private String title;

    private String content;

    private Long commentCount;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static BoardListResponseDto toDto(Board board, MemberDetailedInfoDto memberInfo, Long commentCount) {
        WriterInfo writerInfo = WriterInfo.fromBoard(board, memberInfo);
        return BoardListResponseDto.builder()
                .id(board.getId())
                .writerId(writerInfo.getId())
                .writerName(writerInfo.getName())
                .category(board.getCategory().getKey())
                .title(board.getTitle())
                .content(board.getContent())
                .commentCount(commentCount)
                .imageUrl(board.getImageUrl())
                .createdAt(board.getCreatedAt())
                .build();
    }

}
