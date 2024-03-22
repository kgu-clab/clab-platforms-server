package page.clab.api.domain.book.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.book.domain.Book;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequestDto {

    @NotNull(message = "{notNull.book.category}")
    @Schema(description = "카테고리", example = "IT/개발", required = true)
    private String category;

    @NotNull(message = "{notNull.book.title}")
    @Schema(description = "제목", example = "스프링 부트와 AWS로 혼자 구현하는 웹 서비스", required = true)
    private String title;

    @NotNull(message = "{notNull.book.author}")
    @Schema(description = "저자", example = "이동욱", required = true)
    private String author;

    @NotNull(message = "{notNull.book.publisher}")
    @Schema(description = "출판사", example = "프리렉", required = true)
    private String publisher;

    @Schema(description = "이미지 URL", example = "https://shopping-phinf.pstatic.net/main_3243625/32436253723.20230928091945.jpg?type=w300")
    private String imageUrl;

    public static Book toEntity(BookRequestDto requestDto) {
        return Book.builder()
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .publisher(requestDto.getPublisher())
                .imageUrl(requestDto.getImageUrl())
                .build();
    }

}
