package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.dto.shared.MemberEmailInfoDto;
import page.clab.api.domain.member.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.global.auth.util.AuthUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInfoRetrievalService implements RetrieveMemberInfoUseCase {

    private final LoadMemberPort loadMemberPort;

    @Override
    public List<MemberEmailInfoDto> getMembers() {
        List<Member> members = loadMemberPort.findAll();
        return members.stream()
                .map(MemberEmailInfoDto::create)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMemberIds() {
        return loadMemberPort.findAll()
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAdminIds() {
        return loadMemberPort.findAll()
                .stream()
                .filter(Member::isAdminRole)
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSuperAdminIds() {
        return loadMemberPort.findAll()
                .stream()
                .filter(Member::isSuperAdminRole)
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Override
    public MemberBasicInfoDto getMemberBasicInfoById(String memberId) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        return MemberBasicInfoDto.create(member);
    }

    @Override
    public MemberBasicInfoDto getCurrentMemberBasicInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = loadMemberPort.findByIdOrThrow(currentMemberId);
        return MemberBasicInfoDto.create(member);
    }

    @Override
    public MemberDetailedInfoDto getMemberDetailedInfoById(String memberId) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        return MemberDetailedInfoDto.create(member);
    }

    @Override
    public MemberDetailedInfoDto getCurrentMemberDetailedInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = loadMemberPort.findByIdOrThrow(currentMemberId);
        return MemberDetailedInfoDto.create(member);
    }

    @Override
    public MemberBorrowerInfoDto getCurrentMemberBorrowerInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = loadMemberPort.findByIdOrThrow(currentMemberId);
        return MemberBorrowerInfoDto.create(member);
    }

    @Override
    public MemberLoginInfoDto getMemberLoginInfoById(String memberId) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        return MemberLoginInfoDto.create(member);
    }

    @Override
    public MemberPositionInfoDto getCurrentMemberPositionInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = loadMemberPort.findByIdOrThrow(currentMemberId);
        return MemberPositionInfoDto.create(member);
    }
}
