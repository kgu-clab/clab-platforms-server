package page.clab.api.domain.members.donation.application.port.in;

import page.clab.api.domain.members.donation.application.dto.request.DonationRequestDto;

public interface RegisterDonationUseCase {
    Long registerDonation(DonationRequestDto requestDto);
}
