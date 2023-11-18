package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.GroupMemberStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {

    Optional<GroupMember> findByMember(Member member);

    Optional<GroupMember> findByActivityGroupIdAndRole(Long groupId, ActivityGroupRole role);

    List<GroupMember> findAllByActivityGroupId(Long activityGroupId);

    List<GroupMember> findAllByActivityGroupIdAndStatus(Long activityGroupId, GroupMemberStatus status);
    
}
