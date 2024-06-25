package page.clab.api.domain.book.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.book.domain.Book;

import java.time.LocalDateTime;

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

    public static BookResponseDto toDto(Book book, String borrowerName, LocalDateTime dueDate) {
        return BookResponseDto.builder()
                .id(book.getId())
                .borrowerId(book.getBorrowerId() == null ? null : book.getBorrowerId())
                .borrowerName(book.getBorrowerId() == null ? null : borrowerName)
                .category(book.getCategory())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .imageUrl(book.getImageUrl())
                .dueDate(dueDate)
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

}
