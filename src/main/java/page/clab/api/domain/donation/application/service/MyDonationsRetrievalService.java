package page.clab.api.domain.donation.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.dto.response.DonationResponseDto;
import page.clab.api.domain.donation.application.port.in.RetrieveMyDonationsUseCase;
import page.clab.api.domain.donation.application.port.out.RetrieveDonationPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyDonationsRetrievalService implements RetrieveMyDonationsUseCase {

    private final RetrieveDonationPort retrieveDonationPort;
    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DonationResponseDto> retrieveMyDonations(Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<Donation> donations = retrieveDonationPort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = retrieveMemberInfoUseCase.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }
}
