package page.clab.api.domain.library.book.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.library.book.domain.Book;

import java.time.LocalDateTime;
import java.util.List;

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

    public static BookDetailsResponseDto toDto(Book book, String borrowerName, LocalDateTime dueDate) {
        return BookDetailsResponseDto.builder()
                .id(book.getId())
                .borrowerId(book.getBorrowerId() == null ? null : book.getBorrowerId())
                .borrowerName(book.getBorrowerId() == null ? null : borrowerName)
                .category(book.getCategory())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .imageUrl(book.getImageUrl())
                .reviewLinks(book.getReviewLinks())
                .dueDate(dueDate)
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
