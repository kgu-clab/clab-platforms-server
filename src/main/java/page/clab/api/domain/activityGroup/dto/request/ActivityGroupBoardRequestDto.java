package page.clab.api.domain.activityGroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
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

    @Size(min = 1, max = 50, message = "{size.board.category}")
    @Schema(description = "카테고리", example = "공지사항")
    private String category;

    @Size(min = 1, max = 100, message = "{size.board.title}")
    @Schema(description = "제목", example = "C언어 스터디 과제 제출 관련 공지")
    private String title;

    @Schema(description = "내용", example = "C언어 스터디 과제 제출 관련 공지")
    private String content;

    @Schema(description = "과제 제출 파일 경로", example = "/resources/files/assignment/1/1/superuser/339609571877700_4305d83e-090a-480b-a470-b5e96164d113.png")
    private List<String> fileUrlList;

    @Schema(description = "마감일자", example = "2024-11-28 18:00:00.000")
    private LocalDateTime dueDateTime;

}
