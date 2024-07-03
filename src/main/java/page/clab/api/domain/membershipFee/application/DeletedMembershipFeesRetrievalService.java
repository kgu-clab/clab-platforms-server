package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.membershipFee.application.port.in.DeletedMembershipFeesRetrievalUseCase;
import page.clab.api.domain.membershipFee.application.port.out.RetrieveDeletedMembershipFeesPort;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedMembershipFeesRetrievalService implements DeletedMembershipFeesRetrievalUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final RetrieveDeletedMembershipFeesPort retrieveDeletedMembershipFeesPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MembershipFeeResponseDto> retrieve(Pageable pageable) {
        MemberDetailedInfoDto memberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        Page<MembershipFee> membershipFees = retrieveDeletedMembershipFeesPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(membershipFees.map(membershipFee ->
                MembershipFeeResponseDto.toDto(membershipFee, memberInfo.getMemberName(), memberInfo.isAdminRole())));
    }
}
