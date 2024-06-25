package page.clab.api.domain.member.application;

import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.domain.member.dto.shared.MemberInfoDto;

import java.util.List;

public interface MemberLookupService {

    boolean existsMemberById(String memberId);

    Member getMemberById(String memberId);

    Member getMemberByIdOrThrow(String memberId);

    Member getMemberByEmail(String email);

    Member getCurrentMember();

    String getCurrentMemberId();

    List<MemberResponseDto> getMembers();

    List<Member> findAllMembers();

    List<Member> getAdmins();

    List<Member> getSuperAdmins();

    MemberInfoDto getMemberInfoById(String memberId);

    MemberInfoDto getCurrentMemberInfo();

}
