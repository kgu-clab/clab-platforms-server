package page.clab.api.domain.library.book.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDetailsResponseDto {

    private Long id;
    private String borrowerId;
    private String borrowerName;
    private String category;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;
    private List<String> reviewLinks;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
