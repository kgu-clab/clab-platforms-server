package page.clab.api.domain.membershipFee.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.membershipFee.application.MembershipFeeUpdateService;
import page.clab.api.domain.membershipFee.dao.MembershipFeeRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class MembershipFeeUpdateServiceImpl implements MembershipFeeUpdateService {

    private final MemberLookupService memberLookupService;
    private final ValidationService validationService;
    private final MembershipFeeRepository membershipFeeRepository;

    @Transactional
    @Override
    public Long update(Long membershipFeeId, MembershipFeeUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMemberInfo);
        membershipFee.update(requestDto);
        validationService.checkValid(membershipFee);
        return membershipFeeRepository.save(membershipFee).getId();
    }

    private MembershipFee getMembershipFeeByIdOrThrow(Long membershipFeeId) {
        return membershipFeeRepository.findById(membershipFeeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }
}