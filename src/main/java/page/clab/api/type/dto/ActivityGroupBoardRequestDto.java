package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupBoardRequestDto {

    @NotNull(message = "{notNull.board.category}")
    @Size(min = 1, max = 50, message = "{size.board.category}")
    @Schema(description = "카테고리", example = "공지사항", required = true)
    private String category;

    @NotNull(message = "{notNull.board.title}")
    @Size(min = 1, max = 100, message = "{size.board.title}")
    @Schema(description = "제목", example = "C언어 스터디 과제 제출 관련 공지", required = true)
    private String title;

    @NotNull(message = "{notNull.board.content}")
    @Schema(description = "내용", example = "C언어 스터디 과제 제출 관련 공지", required = true)
    private String content;

    @URL(message = "{url.book.imageUrl}")
    @Schema(description = "과제 제출 파일 경로", example = "https://shopping-phinf.pstatic.net/main_3243625/32436253723.20230928091945.jpg?type=w300")
    private String filePath;

    @NotNull(message = "{notNull.board.title}")
    @Size(min = 1, max = 100, message = "{size.board.title}")
    @Schema(description = "과제 제목", example = "C언어 3주차 과제", required = true)
    private String fileName;

}
