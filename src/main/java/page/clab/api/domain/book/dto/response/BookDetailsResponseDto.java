package page.clab.api.domain.book.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.book.domain.Book;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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

    public static BookDetailsResponseDto toDto(Book book, LocalDateTime dueDate) {
        return BookDetailsResponseDto.builder()
                .id(book.getId())
                .borrowerId(book.getBorrower() == null ? null : book.getBorrower().getId())
                .borrowerName(book.getBorrower() == null ? null : book.getBorrower().getName())
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
