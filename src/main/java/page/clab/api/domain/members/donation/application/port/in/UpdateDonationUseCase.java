package page.clab.api.domain.members.donation.application.port.in;

import page.clab.api.domain.members.donation.application.dto.request.DonationUpdateRequestDto;

public interface UpdateDonationUseCase {

    Long updateDonation(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto);
}
