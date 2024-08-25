package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.domain.Member;

import java.util.List;

@Getter
@Builder
public class LeaderInfo {

    private String id;
    private String name;

    public static List<LeaderInfo> create(List<Member> leaders) {
        return leaders.stream()
                .map(LeaderInfo::create)
                .toList();
    }

    public static LeaderInfo create(Member leader) {
        return LeaderInfo.builder()
                .id(leader.getId())
                .name(leader.getName())
                .build();
    }
}
