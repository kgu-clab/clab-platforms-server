package page.clab.api.domain.activityGroup.dao;

import page.clab.api.domain.activityGroup.domain.GroupMember;

public interface GroupMemberRepositoryCustom {

    long countAcceptedMembersByActivityGroupId(Long activityGroupId);

    GroupMember findLeaderByActivityGroupId(Long activityGroupId);

}
