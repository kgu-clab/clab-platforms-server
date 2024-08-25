package page.clab.api.domain.activity.activitygroup.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@Builder
public class LeaderInfo {

    private String id;
    private String name;

    @JsonIgnore
    private LocalDateTime createdAt;

    public static LeaderInfo create(Member leader, LocalDateTime createdAt) {
        return LeaderInfo.builder()
                .id(leader.getId())
                .name(leader.getName())
                .createdAt(createdAt)
                .build();
    }
}
