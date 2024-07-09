package page.clab.api.domain.book.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.QBook;
import page.clab.api.domain.member.domain.QMember;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable) {
        QBook book = QBook.book;
        QMember borrower = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null) builder.and(book.title.containsIgnoreCase(title));
        if (category != null) builder.and(book.category.eq(category));
        if (publisher != null) builder.and(book.publisher.containsIgnoreCase(publisher));
        if (borrowerId != null) builder.and(borrower.id.eq(borrowerId));
        if (borrowerName != null) builder.and(borrower.name.eq(borrowerName));

        List<Book> books = queryFactory.selectFrom(book)
                .leftJoin(borrower).on(book.borrowerId.eq(borrower.id))
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, book))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(book)
                .leftJoin(borrower).on(book.borrowerId.eq(borrower.id))
                .where(builder)
                .fetchCount();

        return new PageImpl<>(books, pageable, count);
    }
}
