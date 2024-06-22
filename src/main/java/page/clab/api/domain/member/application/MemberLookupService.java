package page.clab.api.domain.member.application;

import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;

import java.util.List;

public interface MemberLookupService {

    boolean existsMemberById(String memberId);

    Member getMemberById(String memberId);

    Member getMemberByIdOrThrow(String memberId);

    Member getMemberByEmail(String email);

    Member getCurrentMember();

    List<MemberResponseDto> getMembers();

    List<Member> findAllMembers();

    List<Member> getAdmins();

    List<Member> getSuperAdmins();

}
