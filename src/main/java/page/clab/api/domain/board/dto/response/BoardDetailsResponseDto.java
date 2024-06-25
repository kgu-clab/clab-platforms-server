package page.clab.api.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardDetailsResponseDto {

    private Long id;

    private String writerId;

    private String writerName;

    private Long writerRoleLevel;

    private String writerImageUrl;

    private String category;

    private String title;

    private String content;

    private List<UploadedFileResponseDto> files;

    private String imageUrl;

    private Long likes;

    private boolean hasLikeByMe;

    @JsonProperty("isOwner")
    private Boolean isOwner;

    private LocalDateTime createdAt;

    public static BoardDetailsResponseDto toDto(Board board, Member member, boolean hasLikeByMe, boolean isOwner) {
        WriterInfo writerInfo = WriterInfo.fromBoardDetails(board, member);
        return BoardDetailsResponseDto.builder()
                .id(board.getId())
                .writerId(writerInfo.getId())
                .writerName(writerInfo.getName())
                .writerRoleLevel(writerInfo.getRoleLevel())
                .writerImageUrl(writerInfo.getImageUrl())
                .category(board.getCategory().getKey())
                .title(board.getTitle())
                .content(board.getContent())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .imageUrl(board.getImageUrl())
                .likes(board.getLikes())
                .hasLikeByMe(hasLikeByMe)
                .isOwner(isOwner)
                .createdAt(board.getCreatedAt())
                .build();
    }

}
