package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.application.dto.shared.MemberEmailInfoDto;
import page.clab.api.domain.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.global.auth.util.AuthUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInfoRetrievalService implements RetrieveMemberInfoUseCase {

    private final RetrieveMemberPort retrieveMemberPort;

    @Override
    public List<MemberEmailInfoDto> getMembers() {
        List<Member> members = retrieveMemberPort.findAll();
        return members.stream()
                .map(MemberEmailInfoDto::create)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMemberIds() {
        return retrieveMemberPort.findAll()
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAdminIds() {
        return retrieveMemberPort.findAll()
                .stream()
                .filter(Member::isAdminRole)
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSuperAdminIds() {
        return retrieveMemberPort.findAll()
                .stream()
                .filter(Member::isSuperAdminRole)
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Override
    public MemberBasicInfoDto getMemberBasicInfoById(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        return MemberBasicInfoDto.create(member);
    }

    @Override
    public MemberBasicInfoDto getCurrentMemberBasicInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = retrieveMemberPort.findByIdOrThrow(currentMemberId);
        return MemberBasicInfoDto.create(member);
    }

    @Override
    public MemberDetailedInfoDto getMemberDetailedInfoById(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        return MemberDetailedInfoDto.create(member);
    }

    @Override
    public MemberDetailedInfoDto getCurrentMemberDetailedInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = retrieveMemberPort.findByIdOrThrow(currentMemberId);
        return MemberDetailedInfoDto.create(member);
    }

    @Override
    public MemberBorrowerInfoDto getCurrentMemberBorrowerInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = retrieveMemberPort.findByIdOrThrow(currentMemberId);
        return MemberBorrowerInfoDto.create(member);
    }

    @Override
    public MemberLoginInfoDto getMemberLoginInfoById(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        return MemberLoginInfoDto.create(member);
    }

    @Override
    public MemberPositionInfoDto getCurrentMemberPositionInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = retrieveMemberPort.findByIdOrThrow(currentMemberId);
        return MemberPositionInfoDto.create(member);
    }
}
