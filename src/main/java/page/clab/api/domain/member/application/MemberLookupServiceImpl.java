package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.global.auth.util.AuthUtil;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberLookupServiceImpl implements MemberLookupService {

    private final MemberRepository memberRepository;

    @Override
    public void ensureMemberExists(String memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("[Member] id: " + memberId + "에 해당하는 멤버가 존재하지 않습니다.");
        }
    }

    @Override
    public Member getMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElse(null);
    }

    @Override
    public Member getMemberByIdOrThrow(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + memberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일을 사용하는 멤버가 없습니다."));
    }

    @Override
    public Member getCurrentMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + memberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public String getCurrentMemberId() {
        return AuthUtil.getAuthenticationInfoMemberId();
    }

    @Override
    public List<MemberResponseDto> getMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponseDto::toDto)
                .toList();
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public List<String> getAdminIds() {
        return memberRepository.findAll()
                .stream()
                .filter(Member::isAdminRole)
                .map(Member::getId)
                .toList();
    }

    @Override
    public List<String> getSuperAdminIds() {
        return memberRepository.findAll()
                .stream()
                .filter(Member::isSuperAdminRole)
                .map(Member::getId)
                .toList();
    }

    @Override
    public MemberBasicInfoDto getMemberBasicInfoById(String memberId) {
        return memberRepository.findById(memberId)
                .map(MemberBasicInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + memberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public MemberBasicInfoDto getCurrentMemberBasicInfo() {
        String currentMemberId = getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .map(MemberBasicInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + currentMemberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public MemberDetailedInfoDto getMemberDetailedInfoById(String memberId) {
        return memberRepository.findById(memberId)
                .map(MemberDetailedInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + memberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public MemberDetailedInfoDto getCurrentMemberDetailedInfo() {
        String currentMemberId = getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .map(MemberDetailedInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + currentMemberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public MemberBorrowerInfoDto getCurrentMemberBorrowerInfo() {
        String currentMemberId = getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .map(MemberBorrowerInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + currentMemberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public MemberLoginInfoDto getMemberLoginInfoById(String memberId) {
        return memberRepository.findById(memberId)
                .map(MemberLoginInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + memberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public MemberPositionInfoDto getCurrentMemberPositionInfo() {
        String currentMemberId = getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .map(MemberPositionInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + currentMemberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    public void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate) {
        Member member = getMemberByIdOrThrow(memberId);
        member.updateLoanSuspensionDate(loanSuspensionDate);
        memberRepository.save(member);
    }

    @Override
    public void updateLastLoginTime(String id) {
        Member member = getMemberByIdOrThrow(id);
        member.updateLastLoginTime();
        memberRepository.save(member);
    }

}
