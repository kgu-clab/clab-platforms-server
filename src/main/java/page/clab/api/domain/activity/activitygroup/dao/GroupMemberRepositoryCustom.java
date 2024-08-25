package page.clab.api.domain.activity.activitygroup.dao;

import page.clab.api.domain.activity.activitygroup.domain.GroupMember;

import java.util.List;

public interface GroupMemberRepositoryCustom {

    long countAcceptedMembersByActivityGroupId(Long activityGroupId);

    List<GroupMember> findLeaderByActivityGroupId(Long activityGroupId);
}
