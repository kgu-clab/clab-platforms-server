package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.dto.shared.MemberEmailInfoDto;
import page.clab.api.domain.member.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;

import java.util.List;

public interface RetrieveMemberInfoUseCase {
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
}
