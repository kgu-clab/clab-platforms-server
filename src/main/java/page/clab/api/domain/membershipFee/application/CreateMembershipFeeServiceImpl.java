package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.membershipFee.dao.MembershipFeeRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateMembershipFeeServiceImpl implements CreateMembershipFeeService {

    private final MemberLookupService memberLookupService;
    private final NotificationService notificationService;
    private final ValidationService validationService;
    private final MembershipFeeRepository membershipFeeRepository;

    @Transactional
    @Override
    public Long execute(MembershipFeeRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        MembershipFee membershipFee = MembershipFeeRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(membershipFee);
        notificationService.sendNotificationToAdmins("새로운 회비 내역이 등록되었습니다.");
        return membershipFeeRepository.save(membershipFee).getId();
    }
}
