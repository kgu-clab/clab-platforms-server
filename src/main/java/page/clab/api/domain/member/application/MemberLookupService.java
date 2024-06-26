package page.clab.api.domain.member.application;

import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.domain.member.dto.shared.BookBorrowerInfoDto;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberLookupService {

    Member getMemberById(String memberId);

    Member getMemberByIdOrThrow(String memberId);

    Member getMemberByEmail(String email);

    Member getCurrentMember();

    String getCurrentMemberId();

    List<MemberResponseDto> getMembers();

    List<Member> findAllMembers();

    List<Member> getAdmins();

    List<Member> getSuperAdmins();

    MemberBasicInfoDto getMemberBasicInfoById(String memberId);

    MemberBasicInfoDto getCurrentMemberBasicInfo();

    MemberDetailedInfoDto getMemberDetailedInfoById(String memberId);

    MemberDetailedInfoDto getCurrentMemberDetailedInfo();

    BookBorrowerInfoDto getCurrentMemberBorrowerInfo();

    void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate);

}
