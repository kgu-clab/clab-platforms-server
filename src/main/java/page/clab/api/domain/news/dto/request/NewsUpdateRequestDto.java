package page.clab.api.domain.news.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NewsUpdateRequestDto {

    @Schema(description = "제목", example = "컴퓨터공학과, SW 개발보안 경진대회 최우수상, 우수상 수상")
    private String title;

    @Schema(description = "카테고리", example = "동아리 소식")
    private String category;

    @Schema(description = "내용", example = "컴퓨터공학과, SW 개발보안 경진대회 최우수상, 우수상 수상")
    private String content;

    @Schema(description = "URL", example = "https://blog.naver.com/kyonggi_love/223199431495")
    private String articleUrl;

    @Schema(description = "출처", example = "경기대학교 공식 블로그")
    private String source;

    @Schema(description = "날짜", example = "2021-08-31")
    private LocalDate date;
}
