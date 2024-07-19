package page.clab.api.external.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(String memberId) {
        return checkMemberExistencePort.existsById(memberId);
    }

    @Transactional(readOnly = true)
    @Override
    public void ensureMemberExists(String memberId) {
        if (!checkMemberExistencePort.existsById(memberId)) {
            throw new NotFoundException("[Member] id: " + memberId + "에 해당하는 회원이 존재하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Member> findById(String id) {
        return retrieveMemberPort.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Member findByIdOrThrow(String memberId) {
        return retrieveMemberPort.findByIdOrThrow(memberId);
    }

    @Transactional(readOnly = true)
    @Override
    public Member findByEmail(String address) {
        return retrieveMemberPort.findByEmailOrThrow(address);
    }

    @Transactional(readOnly = true)
    @Override
    public Member getCurrentMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return retrieveMemberPort.findByIdOrThrow(memberId);
    }

    @Transactional(readOnly = true)
    @Override
    public String getCurrentMemberId() {
        return AuthUtil.getAuthenticationInfoMemberId();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberEmailInfoDto> getMembers() {
        List<Member> members = retrieveMemberPort.findAll();
        return members.stream()
                .map(MemberEmailInfoDto::create)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getMemberIds() {
        return retrieveMemberPort.findAll()
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getAdminIds() {
        return retrieveMemberPort.findAll()
                .stream()
                .filter(Member::isAdminRole)
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getSuperAdminIds() {
        return retrieveMemberPort.findAll()
                .stream()
                .filter(Member::isSuperAdminRole)
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public MemberBasicInfoDto getMemberBasicInfoById(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        return MemberBasicInfoDto.create(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberBasicInfoDto getCurrentMemberBasicInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = retrieveMemberPort.findByIdOrThrow(currentMemberId);
        return MemberBasicInfoDto.create(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDetailedInfoDto getMemberDetailedInfoById(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        return MemberDetailedInfoDto.create(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDetailedInfoDto getCurrentMemberDetailedInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = retrieveMemberPort.findByIdOrThrow(currentMemberId);
        return MemberDetailedInfoDto.create(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberBorrowerInfoDto getCurrentMemberBorrowerInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = retrieveMemberPort.findByIdOrThrow(currentMemberId);
        return MemberBorrowerInfoDto.create(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberLoginInfoDto getMemberLoginInfoById(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        return MemberLoginInfoDto.create(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberPositionInfoDto getCurrentMemberPositionInfo() {
        String currentMemberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = retrieveMemberPort.findByIdOrThrow(currentMemberId);
        return MemberPositionInfoDto.create(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberReviewInfoDto getMemberReviewInfoById(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        return MemberReviewInfoDto.create(member);
    }

}
