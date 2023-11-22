package page.clab.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.GroupMemberStatus;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {

    Optional<GroupMember> findByMember(Member member);

    Optional<GroupMember> findByActivityGroupIdAndRole(Long groupId, ActivityGroupRole role);

    List<GroupMember> findAllByActivityGroupIdOrderByMemberAsc(Long activityGroupId);
    
    Page<GroupMember> findAllByActivityGroupIdOrderByMemberAsc(Long activityGroupId, Pageable pageable);

    Page<GroupMember> findAllByActivityGroupIdAndStatusOrderByMemberAsc(Long activityGroupId, GroupMemberStatus status, Pageable pageable);
    
}
