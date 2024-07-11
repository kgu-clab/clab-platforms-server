package page.clab.api.domain.book.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.book.domain.Book;

@Component
public class BookMapper {

    public BookJpaEntity toJpaEntity(Book book) {
        return BookJpaEntity.builder()
                .id(book.getId())
                .category(book.getCategory())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .imageUrl(book.getImageUrl())
                .reviewLinks(book.getReviewLinks())
                .borrowerId(book.getBorrowerId())
                .version(book.getVersion())
                .isDeleted(book.isDeleted())
                .build();
    }

    public Book toDomain(BookJpaEntity entity) {
        return Book.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .publisher(entity.getPublisher())
                .imageUrl(entity.getImageUrl())
                .reviewLinks(entity.getReviewLinks())
                .borrowerId(entity.getBorrowerId())
                .version(entity.getVersion())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isDeleted(entity.isDeleted())
                .build();
    }
}
