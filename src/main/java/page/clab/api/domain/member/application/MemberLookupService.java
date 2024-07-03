package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.port.in.MemberLookupUseCase;
import page.clab.api.domain.member.application.port.out.CheckMemberExistencePort;
import page.clab.api.domain.member.application.port.out.LoadMemberByEmailPort;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.dto.shared.MemberEmailInfoDto;
import page.clab.api.domain.member.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.global.auth.util.AuthUtil;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberLookupService implements MemberLookupUseCase {

    private final CheckMemberExistencePort checkMemberExistencePort;
    private final LoadMemberPort loadMemberPort;
    private final LoadMemberByEmailPort LoadMemberByEmailPort;
    private final RegisterMemberPort registerMemberPort;

    @Override
    public void ensureMemberExists(String memberId) {
        if (!checkMemberExistencePort.existsById(memberId)) {
            throw new NotFoundException("[Member] id: " + memberId + "에 해당하는 회원이 존재하지 않습니다.");
        }
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return loadMemberPort.findById(memberId);
    }

    @Override
    public Member findByIdOrThrow(String memberId) {
        return loadMemberPort.findByIdOrThrow(memberId);
    }

    @Override
    public Member findByEmail(String email) {
        return LoadMemberByEmailPort.findByEmailOrThrow(email);
    }

    @Override
    public Member getCurrentMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return loadMemberPort.findByIdOrThrow(memberId);
    }

    @Override
    public String getCurrentMemberId() {
        return AuthUtil.getAuthenticationInfoMemberId();
    }

    @Override
    public List<MemberEmailInfoDto> getMembers() {
        List<Member> members = loadMemberPort.findAll();
        return members.stream()
                .map(MemberEmailInfoDto::create)
                .toList();
    }

    @Override
    public List<String> getMemberIds() {
        return loadMemberPort.findAll()
                .stream()
                .map(Member::getId)
                .toList();
    }

    @Override
    public List<String> getAdminIds() {
        return loadMemberPort.findAll()
                .stream()
                .filter(Member::isAdminRole)
                .map(Member::getId)
                .toList();
    }

    @Override
    public List<String> getSuperAdminIds() {
        return loadMemberPort.findAll()
                .stream()
                .filter(Member::isSuperAdminRole)
                .map(Member::getId)
                .toList();
    }

    @Override
    public MemberBasicInfoDto getMemberBasicInfoById(String memberId) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        return MemberBasicInfoDto.create(member);
    }

    @Override
    public MemberBasicInfoDto getCurrentMemberBasicInfo() {
        String currentMemberId = getCurrentMemberId();
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
        String currentMemberId = getCurrentMemberId();
        Member member = loadMemberPort.findByIdOrThrow(currentMemberId);
        return MemberDetailedInfoDto.create(member);
    }

    @Override
    public MemberBorrowerInfoDto getCurrentMemberBorrowerInfo() {
        String currentMemberId = getCurrentMemberId();
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
        String currentMemberId = getCurrentMemberId();
        Member member = loadMemberPort.findByIdOrThrow(currentMemberId);
        return MemberPositionInfoDto.create(member);
    }

    public void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        member.updateLoanSuspensionDate(loanSuspensionDate);
        registerMemberPort.save(member);
    }

    @Override
    public void updateLastLoginTime(String memberId) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        member.updateLastLoginTime();
        registerMemberPort.save(member);
    }

}
