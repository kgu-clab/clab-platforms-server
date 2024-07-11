package page.clab.api.domain.book.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.book.application.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.book.application.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.member.adapter.out.persistence.QMemberJpaEntity;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookLoanRecordRepositoryImpl implements BookLoanRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookLoanRecordResponseDto> findByConditions(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable) {
        QBookLoanRecordJpaEntity bookLoanRecord = QBookLoanRecordJpaEntity.bookLoanRecordJpaEntity;
        QBookJpaEntity book = QBookJpaEntity.bookJpaEntity;
        QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;

        BooleanBuilder builder = new BooleanBuilder();
        if (bookId != null) builder.and(bookLoanRecord.bookId.eq(bookId));
        if (borrowerId != null && !borrowerId.trim().isEmpty()) builder.and(bookLoanRecord.borrowerId.eq(borrowerId));
        if (status != null) builder.and(bookLoanRecord.status.eq(status));

        List<BookLoanRecordResponseDto> results = queryFactory
                .select(Projections.constructor(
                        BookLoanRecordResponseDto.class,
                        bookLoanRecord.id,
                        bookLoanRecord.bookId,
                        book.title,
                        book.imageUrl,
                        bookLoanRecord.borrowerId,
                        member.name.as("borrowerName"),
                        bookLoanRecord.borrowedAt,
                        bookLoanRecord.returnedAt,
                        bookLoanRecord.dueDate,
                        bookLoanRecord.loanExtensionCount,
                        bookLoanRecord.status
                ))
                .from(bookLoanRecord)
                .leftJoin(book).on(bookLoanRecord.bookId.eq(book.id))
                .leftJoin(member).on(bookLoanRecord.borrowerId.eq(member.id))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, bookLoanRecord))
                .fetch();

        long total = queryFactory
                .selectFrom(bookLoanRecord)
                .leftJoin(book).on(bookLoanRecord.bookId.eq(book.id))
                .leftJoin(member).on(bookLoanRecord.borrowerId.eq(member.id))
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable) {
        QBookLoanRecordJpaEntity bookLoanRecord = QBookLoanRecordJpaEntity.bookLoanRecordJpaEntity;
        QBookJpaEntity book = QBookJpaEntity.bookJpaEntity;
        QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;

        LocalDateTime now = LocalDateTime.now();

        List<BookLoanRecordOverdueResponseDto> results = queryFactory
                .select(Projections.constructor(
                        BookLoanRecordOverdueResponseDto.class,
                        bookLoanRecord.bookId,
                        book.title,
                        bookLoanRecord.borrowerId,
                        member.name.as("borrowerName"),
                        bookLoanRecord.borrowedAt,
                        bookLoanRecord.dueDate,
                        bookLoanRecord.status
                ))
                .from(bookLoanRecord)
                .leftJoin(book).on(bookLoanRecord.bookId.eq(book.id))
                .leftJoin(member).on(bookLoanRecord.borrowerId.eq(member.id))
                .where(bookLoanRecord.status.eq(BookLoanStatus.APPROVED)
                        .and(bookLoanRecord.dueDate.lt(now)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, bookLoanRecord))
                .fetch();

        long total = queryFactory
                .selectFrom(bookLoanRecord)
                .leftJoin(book).on(bookLoanRecord.bookId.eq(book.id))
                .leftJoin(member).on(bookLoanRecord.borrowerId.eq(member.id))
                .where(bookLoanRecord.status.eq(BookLoanStatus.APPROVED)
                        .and(bookLoanRecord.dueDate.lt(now)))
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
