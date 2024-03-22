package page.clab.api.domain.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.book.domain.Book;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public static BookResponseDto toDto(Book book, LocalDateTime dueDate) {
        return BookResponseDto.builder()
                .id(book.getId())
                .borrowerId(book.getBorrower() == null ? null : book.getBorrower().getId())
                .borrowerName(book.getBorrower() == null ? null : book.getBorrower().getName())
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
