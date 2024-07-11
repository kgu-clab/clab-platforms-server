package page.clab.api.domain.activity.dao;

import page.clab.api.domain.activity.domain.GroupMember;

public interface GroupMemberRepositoryCustom {

    long countAcceptedMembersByActivityGroupId(Long activityGroupId);

    GroupMember findLeaderByActivityGroupId(Long activityGroupId);

}
