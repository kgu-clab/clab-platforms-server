package page.clab.api.domain.donation.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.port.in.RetrieveDeletedDonationsUseCase;
import page.clab.api.domain.donation.application.port.out.RetrieveDonationPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedDonationsRetrievalService implements RetrieveDeletedDonationsUseCase {

    private final RetrieveDonationPort retrieveDonationPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DonationResponseDto> retrieve(Pageable pageable) {
        Page<Donation> donations = retrieveDonationPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = retrieveMemberInfoUseCase.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }
}
