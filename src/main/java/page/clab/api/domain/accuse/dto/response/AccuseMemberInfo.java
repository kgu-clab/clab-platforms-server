package page.clab.api.domain.accuse.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccuseMemberInfo {

    private String memberId;

    private String name;

    private LocalDateTime createdAt;

    public static AccuseMemberInfo create(Member member) {
        return AccuseMemberInfo.builder()
                .memberId(member.getId())
                .name(member.getName())
                .createdAt(member.getCreatedAt())
                .build();
    }

}
