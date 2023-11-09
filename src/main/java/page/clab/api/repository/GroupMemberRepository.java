package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.GroupMember;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    GroupMember findByMemberId(String memberId);

    List<GroupMember> findAllByActivityGroupId(Long activityGroupId);
}
