package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.membershipFee.dao.MembershipFeeRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipFeeService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final MembershipFeeRepository membershipFeeRepository;

    public Long createMembershipFee(MembershipFeeRequestDto membershipFeeRequestDto) {
        Member member = memberService.getCurrentMember();
        MembershipFee membershipFee = MembershipFee.of(membershipFeeRequestDto, member);
        notificationService.sendNotificationToAdmins("새로운 회비 내역이 등록되었습니다.");
        return membershipFeeRepository.save(membershipFee).getId();
    }

    public PagedResponseDto<MembershipFeeResponseDto> getMembershipFeesByConditions(String memberId, String memberName, String category, Pageable pageable) {
        Page<MembershipFee> membershipFeesPage = membershipFeeRepository.findByConditions(memberId, memberName, category, pageable);
        List<MembershipFeeResponseDto> dtos = membershipFeesPage.getContent().stream()
                .map(MembershipFeeResponseDto::of)
                .toList();
        return new PagedResponseDto<>(dtos, pageable, dtos.size());
    }

    public Long updateMembershipFee(Long membershipFeeId, MembershipFeeUpdateRequestDto membershipFeeUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        MembershipFee membershipFee = validateAndGetMembershipFeeForUpdate(membershipFeeId, member);
        membershipFee.update(membershipFeeUpdateRequestDto);
        return membershipFeeRepository.save(membershipFee).getId();
    }

    public Long deleteMembershipFee(Long membershipFeeId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        MembershipFee membershipFee = validateAndGetMembershipFeeForUpdate(membershipFeeId, member);
        membershipFeeRepository.delete(membershipFee);
        return membershipFee.getId();
    }

    private MembershipFee validateAndGetMembershipFeeForUpdate(Long membershipFeeId, Member member) throws PermissionDeniedException {
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        if (!(membershipFee.getApplicant().equals(member) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException();
        }
        return membershipFee;
    }

    private MembershipFee getMembershipFeeByIdOrThrow(Long membershipFeeId) {
        return membershipFeeRepository.findById(membershipFeeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }

}