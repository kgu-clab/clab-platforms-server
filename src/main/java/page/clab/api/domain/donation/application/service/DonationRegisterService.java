package page.clab.api.domain.donation.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.dto.request.DonationRequestDto;
import page.clab.api.domain.donation.application.port.in.RegisterDonationUseCase;
import page.clab.api.domain.donation.application.port.out.RegisterDonationPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class DonationRegisterService implements RegisterDonationUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RegisterDonationPort registerDonationPort;

    @Transactional
    @Override
    public Long registerDonation(DonationRequestDto requestDto) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Donation donation = DonationRequestDto.toEntity(requestDto, currentMemberId);
        return registerDonationPort.save(donation).getId();
    }
}
