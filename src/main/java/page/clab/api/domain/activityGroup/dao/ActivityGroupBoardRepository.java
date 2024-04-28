package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;

import java.util.List;

@Repository
public interface ActivityGroupBoardRepository extends JpaRepository<ActivityGroupBoard, Long>, ActivityGroupBoardRepositoryCustom, QuerydslPredicateExecutor<ActivityGroupBoard> {

    Page<ActivityGroupBoard> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ActivityGroupBoard> findAllByActivityGroup_IdAndCategoryOrderByCreatedAtDesc(Long activityGroupId, ActivityGroupBoardCategory category, Pageable pageable);

    Long countByActivityGroupIdAndCategory(Long id, ActivityGroupBoardCategory category);

    List<ActivityGroupBoard> findAllChildrenByParentId(Long activityGroupBoardId);

}