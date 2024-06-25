package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.domain.member.dto.shared.MemberInfoDto;
import page.clab.api.global.auth.util.AuthUtil;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberLookupServiceImpl implements MemberLookupService {

    private final MemberRepository memberRepository;

    @Override
    public boolean existsMemberById(String memberId) {
        return memberRepository.existsById(memberId);
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
    public List<Member> getAdmins() {
        return memberRepository.findAll()
                .stream()
                .filter(Member::isAdminRole)
                .toList();
    }

    @Override
    public List<Member> getSuperAdmins() {
        return memberRepository.findAll()
                .stream()
                .filter(Member::isSuperAdminRole)
                .toList();
    }

    @Override
    public MemberInfoDto getMemberInfoById(String memberId) {
        return memberRepository.findById(memberId)
                .map(MemberInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + memberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public MemberInfoDto getCurrentMemberInfo() {
        String currentMemberId = getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .map(MemberInfoDto::create)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + currentMemberId + "에 해당하는 멤버가 존재하지 않습니다."));
    }

}
