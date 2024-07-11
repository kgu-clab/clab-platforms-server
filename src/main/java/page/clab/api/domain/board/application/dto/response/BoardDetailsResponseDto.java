package page.clab.api.domain.board.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
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

    @JsonProperty("isOwner")
    private Boolean isOwner;
    private List<BoardEmojiCountResponseDto> emojiInfos;
    private LocalDateTime createdAt;

    public static BoardDetailsResponseDto toDto(Board board, MemberDetailedInfoDto memberInfo, boolean isOwner, List<BoardEmojiCountResponseDto> emojiInfos) {
        WriterInfo writerInfo = WriterInfo.fromBoardDetails(board, memberInfo);
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
                .isOwner(isOwner)
                .emojiInfos(emojiInfos)
                .createdAt(board.getCreatedAt())
                .build();
    }
}
