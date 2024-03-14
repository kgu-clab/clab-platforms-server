package page.clab.api.domain.book.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import page.clab.api.domain.book.domain.Book;
import static page.clab.api.domain.book.domain.QBook.book;

import java.util.List;

public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Book> searchBook(String keyword) {
        return queryFactory
                .selectFrom(book)
                .where(keywordContainsIgnoreCase(keyword))
                .orderBy(book.createdAt.desc())
                .fetch();
    }

    private BooleanExpression keywordContainsIgnoreCase(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }
        String keywordLowerCase = "%" + keyword.toLowerCase() + "%";
        return book.category.toLowerCase().like(keywordLowerCase)
                .or(book.title.toLowerCase().like(keywordLowerCase))
                .or(book.author.toLowerCase().like(keywordLowerCase))
                .or(book.publisher.toLowerCase().like(keywordLowerCase))
                .or(book.borrower.id.equalsIgnoreCase(keyword));
    }

}