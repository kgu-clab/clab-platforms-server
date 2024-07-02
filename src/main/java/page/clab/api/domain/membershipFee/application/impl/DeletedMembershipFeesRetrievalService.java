package page.clab.api.domain.membershipFee.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.membershipFee.application.DeletedMembershipFeesRetrievalUseCase;
import page.clab.api.domain.membershipFee.dao.MembershipFeeRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedMembershipFeesRetrievalService implements DeletedMembershipFeesRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final MembershipFeeRepository membershipFeeRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MembershipFeeResponseDto> retrieve(Pageable pageable) {
        boolean isAdminRole = memberLookupUseCase.getCurrentMemberDetailedInfo().isAdminRole();
        Page<MembershipFee> membershipFees = membershipFeeRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(membershipFees.map(membershipFee -> {
            String applicantName = memberLookupUseCase.getMemberBasicInfoById(membershipFee.getMemberId()).getMemberName();
            return MembershipFeeResponseDto.toDto(membershipFee, applicantName, isAdminRole);
        }));
    }
}