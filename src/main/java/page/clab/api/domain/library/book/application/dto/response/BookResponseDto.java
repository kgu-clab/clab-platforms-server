package page.clab.api.domain.library.book.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookResponseDto {

    private Long id;
    private String borrowerId;
    private String borrowerName;
    private String category;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
