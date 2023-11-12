package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.etc.ActivityGroupRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {

    Optional<GroupMember> findByMemberId(String memberId);

    Optional<GroupMember> findByActivityGroupIdAndRole(Long groupId, ActivityGroupRole role);

    List<GroupMember> findAllByActivityGroupId(Long activityGroupId);
}
