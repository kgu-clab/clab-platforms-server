package page.clab.api.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.util.RandomNicknameUtil;

import java.util.List;

@Getter
@Setter
public class BoardRequestDto {

    @NotNull(message = "{notNull.board.category}")
    @Schema(description = "카테고리", example = "NOTICE", required = true)
    private BoardCategory category;

    @NotNull(message = "{notNull.board.title}")
    @Schema(description = "제목", example = "2023년 2학기 모집 안내", required = true)
    private String title;

    @NotNull(message = "{notNull.board.content}")
    @Schema(description = "내용", example = "2023년 2학기 모집 안내", required = true)
    private String content;

    @Schema(description = "첨부파일 경로 리스트", example = "[\"/resources/files/boards/339609571877700_4305d83e-090a-480b-a470-b5e96164d113.png\", \"/resources/files/boards/4305d83e-090a-480b-a470-b5e96164d114.png\"]")
    private List<String> fileUrlList;

    @Schema(description = "썸네일 이미지 URL", example = "/resources/files/boards/339609571877700_4305d83e-090a-480b-a470-b5e96164d113.png")
    private String imageUrl;

    @NotNull(message = "{notNull.board.wantAnonymous}")
    @Schema(description = "익명 사용 여부", example = "false", required = true)
    private boolean wantAnonymous;

    public static Board toEntity(BoardRequestDto requestDto, String memberId, List<UploadedFile> uploadedFiles) {
        return Board.builder()
                .memberId(memberId)
                .nickname(RandomNicknameUtil.makeRandomNickname())
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .uploadedFiles(uploadedFiles)
                .imageUrl(requestDto.getImageUrl())
                .wantAnonymous(requestDto.isWantAnonymous())
                .build();
    }
}
