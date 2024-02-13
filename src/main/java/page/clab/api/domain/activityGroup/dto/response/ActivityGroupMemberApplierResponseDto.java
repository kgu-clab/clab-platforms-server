package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupMemberApplierResponseDto {

    private String id;

    private String name;

    private String department;

    private Long grade;

    public static ActivityGroupMemberApplierResponseDto of(Member member) {
        return ActivityGroupMemberApplierResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .department(member.getDepartment())
                .grade(member.getGrade())
                .build();
    }

}
