package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.membershipFee.dao.MembershipFeeRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class DeleteMembershipFeeServiceImpl implements DeleteMembershipFeeService {

    private final MemberLookupService memberLookupService;
    private final MembershipFeeRepository membershipFeeRepository;

    @Transactional
    @Override
    public Long execute(Long membershipFeeId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMemberInfo);
        membershipFee.delete();
        membershipFeeRepository.save(membershipFee);
        return membershipFee.getId();
    }

    private MembershipFee getMembershipFeeByIdOrThrow(Long membershipFeeId) {
        return membershipFeeRepository.findById(membershipFeeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }
}