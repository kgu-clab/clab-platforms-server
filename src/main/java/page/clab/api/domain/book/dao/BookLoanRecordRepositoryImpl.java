package page.clab.api.domain.book.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.domain.QBookLoanRecord;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;

import java.util.List;

public class BookLoanRecordRepositoryImpl implements BookLoanRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookLoanRecordRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<BookLoanRecordResponseDto> getBookLoanRecordsByCondition(Long bookId, String borrowerId, Boolean isReturned, Pageable pageable) {
        QBookLoanRecord bookLoanRecord = QBookLoanRecord.bookLoanRecord;

        BooleanBuilder builder = new BooleanBuilder();
        if (bookId != null) {
            builder.and(bookLoanRecord.book.id.eq(bookId));
        }
        if (borrowerId != null && !borrowerId.trim().isEmpty()) {
            builder.and(bookLoanRecord.borrower.id.eq(borrowerId));
        }
        if (isReturned != null) {
            if (isReturned) {
                builder.and(bookLoanRecord.returnedAt.isNotNull());
            } else {
                builder.and(bookLoanRecord.returnedAt.isNull());
            }
        }

        List<BookLoanRecordResponseDto> results = queryFactory
                .select(Projections.constructor(
                        BookLoanRecordResponseDto.class,
                        bookLoanRecord.id,
                        bookLoanRecord.borrower.id,
                        bookLoanRecord.borrowedAt,
                        bookLoanRecord.returnedAt,
                        bookLoanRecord.dueDate,
                        bookLoanRecord.loanExtensionCount
                ))
                .from(bookLoanRecord)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bookLoanRecord.borrowedAt.desc())
                .fetch();

        long total = queryFactory
                .selectFrom(bookLoanRecord)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

}
