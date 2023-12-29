package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.GroupMemberId;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.GroupMemberStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    Optional<GroupMember> findAllByMember(Member member);

    Optional<GroupMember> findByMember(Member member);

    Optional<GroupMember> findByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role);

    List<GroupMember> findAllByActivityGroupIdOrderByMember_IdAsc(Long activityGroupId);

    Page<GroupMember> findAllByActivityGroupIdOrderByMember_IdAsc(Long activityGroupId, Pageable pageable);

    Page<GroupMember> findAllByActivityGroupIdAndStatusOrderByMember_IdAsc(Long activityGroupId, GroupMemberStatus status, org.springframework.data.domain.Pageable pageable);
    
}
