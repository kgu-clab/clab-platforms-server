package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberLookupUseCase;
import page.clab.api.domain.membershipFee.application.port.in.DeletedMembershipFeesRetrievalUseCase;
import page.clab.api.domain.membershipFee.application.port.out.RetrieveDeletedMembershipFeesPort;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedMembershipFeesRetrievalService implements DeletedMembershipFeesRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RetrieveDeletedMembershipFeesPort retrieveDeletedMembershipFeesPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MembershipFeeResponseDto> retrieve(Pageable pageable) {
        boolean isAdminRole = memberLookupUseCase.getCurrentMemberDetailedInfo().isAdminRole();
        Page<MembershipFee> membershipFees = retrieveDeletedMembershipFeesPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(membershipFees.map(membershipFee -> {
            String applicantName = memberLookupUseCase.getMemberBasicInfoById(membershipFee.getMemberId()).getMemberName();
            return MembershipFeeResponseDto.toDto(membershipFee, applicantName, isAdminRole);
        }));
    }
}
