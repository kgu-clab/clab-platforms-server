package page.clab.api.domain.library.book.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.library.book.application.dto.request.BookUpdateRequestDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Book {

    private Long id;
    private String category;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;
    private List<String> reviewLinks;
    private String borrowerId;
    private Long version;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(BookUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(requestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(requestDto.getAuthor()).ifPresent(this::setAuthor);
        Optional.ofNullable(requestDto.getPublisher()).ifPresent(this::setPublisher);
        Optional.ofNullable(requestDto.getImageUrl()).ifPresent(this::setImageUrl);
        Optional.ofNullable(requestDto.getReviewLinks()).ifPresent(this::setReviewLinks);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isBorrower(String borrowerId) {
        return this.borrowerId == null || !this.borrowerId.equals(borrowerId);
    }

    public void validateBookIsNotBorrowed() {
        if (this.borrowerId != null) {
            throw new BaseException(ErrorCode.BOOK_ALREADY_BORROWED);
        }
    }

    public void borrowBook(String borrowerId) {
        validateBookIsNotBorrowed();
        this.borrowerId = borrowerId;
    }

    public void returnBook(String borrowerId) {
        validateCurrentBorrower(borrowerId);
        this.borrowerId = null;
    }

    public void validateCurrentBorrower(String borrowerId) {
        if (isBorrower(borrowerId)) {
            throw new BaseException(ErrorCode.BOOK_BORROWER_MISMATCH);
        }
    }
}
