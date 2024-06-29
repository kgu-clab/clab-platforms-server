package page.clab.api.domain.donation.application;

import page.clab.api.domain.donation.dto.request.DonationRequestDto;

public interface CreateDonationService {
    Long execute(DonationRequestDto requestDto);
}