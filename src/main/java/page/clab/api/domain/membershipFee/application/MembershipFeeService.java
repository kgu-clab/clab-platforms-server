package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.membershipFee.dao.MembershipFeeRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class MembershipFeeService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final MembershipFeeRepository membershipFeeRepository;

    public Long createMembershipFee(MembershipFeeRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        MembershipFee membershipFee = MembershipFeeRequestDto.toEntity(requestDto, currentMember);
        validationService.checkValid(membershipFee);
        notificationService.sendNotificationToAdmins("새로운 회비 내역이 등록되었습니다.");
        return membershipFeeRepository.save(membershipFee).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<MembershipFeeResponseDto> getMembershipFeesByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable) {
        Page<MembershipFee> membershipFeesPage = membershipFeeRepository.findByConditions(memberId, memberName, category, status, pageable);
        return new PagedResponseDto<>(membershipFeesPage.map(MembershipFeeResponseDto::toDto));
    }

    @Transactional
    public Long updateMembershipFee(Long membershipFeeId, MembershipFeeUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMember);
        membershipFee.update(requestDto);
        validationService.checkValid(membershipFee);
        return membershipFeeRepository.save(membershipFee).getId();
    }

    public Long deleteMembershipFee(Long membershipFeeId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMember);
        membershipFeeRepository.delete(membershipFee);
        return membershipFee.getId();
    }

    private MembershipFee getMembershipFeeByIdOrThrow(Long membershipFeeId) {
        return membershipFeeRepository.findById(membershipFeeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }

}