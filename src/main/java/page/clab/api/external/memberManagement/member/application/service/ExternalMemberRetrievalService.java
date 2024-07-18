package page.clab.api.external.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberEmailInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberReviewInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.out.CheckMemberExistencePort;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.auth.util.AuthUtil;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExternalMemberRetrievalService implements ExternalRetrieveMemberUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final CheckMemberExistencePort checkMemberExistencePort;

    @Override
    public boolean existsById(String memberId) {
        return checkMemberExistencePort.existsById(memberId);
    }

    @Override
    public void ensureMemberExists(String memberId) {
        if (!checkMemberExistencePort.existsById(memberId)) {
            throw new NotFoundException("[Member] id: " + memberId + "에 해당하는 회원이 존재하지 않습니다.");
        }
    }

    @Override
    public Optional<Member> findById(String id) {
        return retrieveMemberPort.findById(id);
    }

    @Override
    public Member findByIdOrThrow(String memberId) {
        return retrieveMemberPort.findByIdOrThrow(memberId);
    }

    @Override
    public Member getCurrentMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return retrieveMemberPort.findByIdOrThrow(memberId);
    }

    @Override
    public String getCurrentMemberId() {
        return AuthUtil.getAuthenticationInfoMemberId();
    }

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

    @Override
    public MemberReviewInfoDto getMemberReviewInfoById(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        return MemberReviewInfoDto.create(member);
    }

}
