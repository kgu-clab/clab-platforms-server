package page.clab.api.domain.community.board.adapter.out.persistence;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardHashtagRepositoryImpl implements BoardHashtagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> getBoardIdsByHashTagId(List<Long> hashtagIds, Pageable pageable) {
        QBoardHashtagJpaEntity boardHashtag = QBoardHashtagJpaEntity.boardHashtagJpaEntity;

        JPQLQuery<Long> query = queryFactory.selectDistinct(boardHashtag.boardId)
                .from(boardHashtag)
                .where(boardHashtag.hashtagId.in(hashtagIds)
                        .and(boardHashtag.isDeleted.eq(false)))
                .groupBy(boardHashtag.boardId)
                .having(boardHashtag.hashtagId.count().eq((long) hashtagIds.size()));

        return query.fetch();
    }
}
