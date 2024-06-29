package page.clab.api.domain.donation.application;

import page.clab.api.domain.donation.dto.request.DonationRequestDto;

public interface DonationRegisterService {
    Long register(DonationRequestDto requestDto);
}