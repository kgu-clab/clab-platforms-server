package page.clab.api.domain.memberManagement.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.domain.Member;

@Getter
@Builder
public class MemberReviewInfoDto {

    private String memberId;
    private String memberName;
    private String department;

    public static MemberReviewInfoDto create(Member member) {
        return MemberReviewInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .department(member.getDepartment())
                .build();
    }
}
