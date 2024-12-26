package page.clab.api.domain.library.book.application.dto.mapper;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.library.book.application.dto.request.BookRequestDto;
import page.clab.api.domain.library.book.application.dto.response.BookDetailsResponseDto;
import page.clab.api.domain.library.book.application.dto.response.BookResponseDto;
import page.clab.api.domain.library.book.domain.Book;

@Component
public class BookDtoMapper {

    public Book fromDto(BookRequestDto requestDto) {
        return Book.builder()
            .category(requestDto.getCategory())
            .title(requestDto.getTitle())
            .author(requestDto.getAuthor())
            .publisher(requestDto.getPublisher())
            .imageUrl(requestDto.getImageUrl())
            .reviewLinks(CollectionUtils.isEmpty(requestDto.getReviewLinks()) ? List.of() : requestDto.getReviewLinks())
            .isDeleted(false)
            .build();
    }

    public BookResponseDto toDto(Book book, String borrowerName, LocalDateTime dueDate) {
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

    public BookDetailsResponseDto toDetailsDto(Book book, String borrowerName, LocalDateTime dueDate) {
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
