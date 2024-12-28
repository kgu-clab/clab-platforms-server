package page.clab.api.domain.activity.activitygroup.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;

@Repository
public interface ActivityGroupBoardRepository extends JpaRepository<ActivityGroupBoard, Long>,
    ActivityGroupBoardRepositoryCustom, QuerydslPredicateExecutor<ActivityGroupBoard> {

    Page<ActivityGroupBoard> findAllByActivityGroup_IdAndCategory(Long activityGroupId,
        ActivityGroupBoardCategory category, Pageable pageable);

    Long countByActivityGroupIdAndCategory(Long id, ActivityGroupBoardCategory category);

    List<ActivityGroupBoard> findAllChildrenByParentId(Long activityGroupBoardId);

    boolean existsByParentIdAndCategoryAndMemberId(Long parentId, ActivityGroupBoardCategory category, String memberId);

    @Query(value = "SELECT a.* FROM activity_group_board a WHERE a.is_deleted = true", nativeQuery = true)
    Page<ActivityGroupBoard> findAllByIsDeletedTrue(Pageable pageable);
}
