package page.clab.api.domain.community.board.application.service;

import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 응답 DTO 중 BoardJpaEntity 속성으로 DB 정렬 가능한 필드만 모은 set.
 * commentCount 같은 계산 필드는 제외 — 정렬 조건에 있으면 메모리에서 정렬함.
 * 엔티티에 필드 추가/변경 시 이 set도 함께 갱신할 것.
 */
public final class BoardSortableFields {

    private static final Set<String> DATABASE_SORTABLE_FIELDS =
        Set.of("id", "category", "title", "content", "imageUrl", "createdAt");

    private BoardSortableFields() {
    }

    public static boolean canSortInDatabase(Pageable pageable) {
        return pageable.getSort().stream()
            .map(Sort.Order::getProperty)
            .allMatch(DATABASE_SORTABLE_FIELDS::contains);
    }
}
