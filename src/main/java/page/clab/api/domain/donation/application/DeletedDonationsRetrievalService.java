package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.port.in.DeletedDonationsRetrievalUseCase;
import page.clab.api.domain.donation.application.port.out.RetrieveDeletedDonationsPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedDonationsRetrievalService implements DeletedDonationsRetrievalUseCase {

    private final RetrieveDeletedDonationsPort retrieveDeletedDonationsPort;
    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DonationResponseDto> retrieve(Pageable pageable) {
        Page<Donation> donations = retrieveDeletedDonationsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = memberInfoRetrievalUseCase.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }
}
