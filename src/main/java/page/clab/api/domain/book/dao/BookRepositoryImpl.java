package page.clab.api.domain.book.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.QBook;
import page.clab.api.domain.member.domain.QMember;

import java.util.List;

public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName) {
        QBook book = QBook.book;
        QMember borrower = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null) builder.and(book.title.containsIgnoreCase(title));
        if (category != null) builder.and(book.category.eq(category));
        if (publisher != null) builder.and(book.publisher.containsIgnoreCase(publisher));
        if (borrowerId != null) builder.and(borrower.id.eq(borrowerId));
        if (borrowerName != null) builder.and(borrower.name.eq(borrowerName));

        return queryFactory.selectFrom(book)
                .leftJoin(book.borrower, borrower).fetchJoin()
                .where(builder)
                .orderBy(book.createdAt.desc())
                .fetch();
    }

}