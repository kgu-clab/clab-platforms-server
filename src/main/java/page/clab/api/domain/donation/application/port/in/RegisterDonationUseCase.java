package page.clab.api.domain.donation.application.port.in;

import page.clab.api.domain.donation.dto.request.DonationRequestDto;

public interface RegisterDonationUseCase {
    Long registerDonation(DonationRequestDto requestDto);
}
