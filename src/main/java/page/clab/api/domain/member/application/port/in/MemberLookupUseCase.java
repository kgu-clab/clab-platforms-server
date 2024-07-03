package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.dto.shared.MemberEmailInfoDto;
import page.clab.api.domain.member.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberLookupUseCase {

    void ensureMemberExists(String memberId);

    Optional<Member> getMemberById(String memberId);

    Member getMemberByIdOrThrow(String memberId);

    Member getMemberByEmail(String email);

    Member getCurrentMember();

    String getCurrentMemberId();

    List<MemberEmailInfoDto> getMembers();

    List<String> getMemberIds();

    List<String> getAdminIds();

    List<String> getSuperAdminIds();

    MemberBasicInfoDto getMemberBasicInfoById(String memberId);

    MemberBasicInfoDto getCurrentMemberBasicInfo();

    MemberDetailedInfoDto getMemberDetailedInfoById(String memberId);

    MemberDetailedInfoDto getCurrentMemberDetailedInfo();

    MemberBorrowerInfoDto getCurrentMemberBorrowerInfo();

    MemberLoginInfoDto getMemberLoginInfoById(String memberId);

    MemberPositionInfoDto getCurrentMemberPositionInfo();

    void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate);

    void updateLastLoginTime(String id);

}
