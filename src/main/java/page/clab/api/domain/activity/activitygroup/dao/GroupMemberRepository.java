package page.clab.api.domain.activity.activitygroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberId;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId>, GroupMemberRepositoryCustom, QuerydslPredicateExecutor<GroupMember> {

    List<GroupMember> findAllByMemberId(String memberId);

    boolean existsByMemberIdAndActivityGroupId(String memberId, Long activityGroupId);

    List<GroupMember> findByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role);

    Optional<GroupMember> findByActivityGroupAndMemberId(ActivityGroup activityGroup, String memberId);

    List<GroupMember> findAllByActivityGroupIdOrderByMemberIdAsc(Long activityGroupId);

    Page<GroupMember> findAllByActivityGroupId(Long activityGroupId, Pageable pageable);

    Page<GroupMember> findAllByActivityGroupIdAndStatus(Long activityGroupId, GroupMemberStatus status, org.springframework.data.domain.Pageable pageable);

    List<GroupMember> findAllByActivityGroupIdAndStatus(Long activityGroupId, GroupMemberStatus status);

    boolean existsByActivityGroupAndMemberId(ActivityGroup activityGroup, String memberId);

    boolean existsByActivityGroupAndMemberIdAndStatus(ActivityGroup activityGroup, String memberId, GroupMemberStatus status);

    boolean existsByActivityGroupIdAndMemberIdAndStatus(Long activityGroupId, String memberId, GroupMemberStatus status);

    // activityGroupIds 리스트에 해당하는 각 ActivityGroup에서, 가장 createdAt이 오래된 GroupMember를 조회합니다.
    @Query(value = "SELECT DISTINCT ON (gm.activity_group_id) * " +
            "FROM group_member gm " +
            "WHERE gm.activity_group_id IN :activityGroupIds " +
            "ORDER BY gm.activity_group_id, gm.created_at ASC", nativeQuery = true)
    List<GroupMember> findFirstByActivityGroupIdIn(@Param("activityGroupIds") List<Long> activityGroupIds);
}
