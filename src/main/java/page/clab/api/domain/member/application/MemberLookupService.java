package page.clab.api.domain.member.application;

import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.domain.member.dto.shared.BookBorrowerInfoDto;
import page.clab.api.domain.member.dto.shared.LoginMemberInfoDto;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberLookupService {

    void ensureMemberExists(String memberId);

    Member getMemberById(String memberId);

    Member getMemberByIdOrThrow(String memberId);

    Member getMemberByEmail(String email);

    Member getCurrentMember();

    String getCurrentMemberId();

    List<MemberResponseDto> getMembers();

    List<Member> findAllMembers();

    List<String> getAdminIds();

    List<String> getSuperAdminIds();

    MemberBasicInfoDto getMemberBasicInfoById(String memberId);

    MemberBasicInfoDto getCurrentMemberBasicInfo();

    MemberDetailedInfoDto getMemberDetailedInfoById(String memberId);

    MemberDetailedInfoDto getCurrentMemberDetailedInfo();

    BookBorrowerInfoDto getCurrentMemberBorrowerInfo();

    LoginMemberInfoDto getLoginMemberInfoById(String memberId);

    void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate);

    void updateLastLoginTime(String id);

}
