package page.clab.api.domain.memberManagement.member.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;

import java.util.List;

@Getter
@Builder
public class MemberRoleInfoResponseDto {

    private String id;
    private String name;
    private Role role;

    public static List<MemberRoleInfoResponseDto> toDto(List<Member> members) {
        return members.stream()
                .map(MemberRoleInfoResponseDto::toDto)
                .toList();
    }

    public static MemberRoleInfoResponseDto toDto(Member member) {
        return MemberRoleInfoResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }
}
