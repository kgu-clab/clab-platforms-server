package page.clab.api.domain.members.donation.application.port.in;

import page.clab.api.domain.members.donation.application.dto.request.DonationUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateDonationUseCase {

    Long updateDonation(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto)
        throws PermissionDeniedException;
}
