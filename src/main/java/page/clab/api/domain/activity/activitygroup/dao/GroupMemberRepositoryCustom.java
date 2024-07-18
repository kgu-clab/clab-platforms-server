package page.clab.api.domain.activity.activitygroup.dao;

import page.clab.api.domain.activity.activitygroup.domain.GroupMember;

public interface GroupMemberRepositoryCustom {

    long countAcceptedMembersByActivityGroupId(Long activityGroupId);

    GroupMember findLeaderByActivityGroupId(Long activityGroupId);
}
