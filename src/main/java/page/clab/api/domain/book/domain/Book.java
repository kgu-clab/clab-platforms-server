package page.clab.api.domain.book.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.book.application.dto.request.BookUpdateRequestDto;
import page.clab.api.domain.book.application.exception.BookAlreadyBorrowedException;
import page.clab.api.domain.book.application.exception.InvalidBorrowerException;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.util.StringJsonConverter;

import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = StringJsonConverter.class)
    private List<String> reviewLinks;

    @Column(name = "member_id")
    private String borrowerId;

    @Version
    private Long version;

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
            throw new BookAlreadyBorrowedException("이미 대출 중인 도서입니다.");
        }
    }

    public void returnBook(String borrowerId) {
        validateCurrentBorrower(borrowerId);
        this.borrowerId = null;
    }

    public void validateCurrentBorrower(String borrowerId) {
        if (isBorrower(borrowerId)) {
            throw new InvalidBorrowerException("대출한 도서와 회원 정보가 일치하지 않습니다.");
        }
    }
}
