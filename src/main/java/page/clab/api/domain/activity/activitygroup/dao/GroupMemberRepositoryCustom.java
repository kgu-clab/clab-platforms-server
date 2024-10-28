package page.clab.api.domain.activity.activitygroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;

import java.util.List;

public interface GroupMemberRepositoryCustom {

    long countAcceptedMembersByActivityGroupId(Long activityGroupId);

    List<GroupMember> findLeaderByActivityGroupId(Long activityGroupId);

    Page<GroupMember> findAllByActivityGroupId(Long activityGroupId, Pageable pageable);
}
