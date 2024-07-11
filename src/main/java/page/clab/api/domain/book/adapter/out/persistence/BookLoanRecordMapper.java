package page.clab.api.domain.book.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.book.domain.BookLoanRecord;

@Component
public class BookLoanRecordMapper {

    public BookLoanRecordJpaEntity toJpaEntity(BookLoanRecord bookLoanRecord) {
        return BookLoanRecordJpaEntity.builder()
                .id(bookLoanRecord.getId())
                .bookId(bookLoanRecord.getBookId())
                .borrowerId(bookLoanRecord.getBorrowerId())
                .borrowedAt(bookLoanRecord.getBorrowedAt())
                .returnedAt(bookLoanRecord.getReturnedAt())
                .dueDate(bookLoanRecord.getDueDate())
                .loanExtensionCount(bookLoanRecord.getLoanExtensionCount())
                .status(bookLoanRecord.getStatus())
                .isDeleted(bookLoanRecord.isDeleted())
                .build();
    }

    public BookLoanRecord toDomain(BookLoanRecordJpaEntity entity) {
        return BookLoanRecord.builder()
                .id(entity.getId())
                .bookId(entity.getBookId())
                .borrowerId(entity.getBorrowerId())
                .borrowedAt(entity.getBorrowedAt())
                .returnedAt(entity.getReturnedAt())
                .dueDate(entity.getDueDate())
                .loanExtensionCount(entity.getLoanExtensionCount())
                .status(entity.getStatus())
                .isDeleted(entity.isDeleted())
                .build();
    }
}
