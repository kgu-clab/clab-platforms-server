package page.clab.api.domain.library.book.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.memberManagement.member.adapter.out.persistence.QMemberJpaEntity;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookJpaEntity> findByConditions(String title, String category, String publisher, String borrowerId,
        String borrowerName, Pageable pageable) {
        QBookJpaEntity book = QBookJpaEntity.bookJpaEntity;
        QMemberJpaEntity borrower = QMemberJpaEntity.memberJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null) {
            builder.and(book.title.containsIgnoreCase(title));
        }
        if (category != null) {
            builder.and(book.category.eq(category));
        }
        if (publisher != null) {
            builder.and(book.publisher.containsIgnoreCase(publisher));
        }
        if (borrowerId != null) {
            builder.and(borrower.id.eq(borrowerId));
        }
        if (borrowerName != null) {
            builder.and(borrower.name.eq(borrowerName));
        }

        List<BookJpaEntity> books = queryFactory.selectFrom(book)
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
