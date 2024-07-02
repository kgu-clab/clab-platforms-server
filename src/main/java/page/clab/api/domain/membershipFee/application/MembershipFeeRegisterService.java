package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.membershipFee.application.port.in.MembershipFeeRegisterUseCase;
import page.clab.api.domain.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.notification.application.port.in.NotificationSenderUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class MembershipFeeRegisterService implements MembershipFeeRegisterUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final NotificationSenderUseCase notificationService;
    private final ValidationService validationService;
    private final RegisterMembershipFeePort registerMembershipFeePort;

    @Transactional
    @Override
    public Long register(MembershipFeeRequestDto requestDto) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        MembershipFee membershipFee = MembershipFeeRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(membershipFee);
        notificationService.sendNotificationToAdmins("새로운 회비 내역이 등록되었습니다.");
        return registerMembershipFeePort.save(membershipFee).getId();
    }
}
