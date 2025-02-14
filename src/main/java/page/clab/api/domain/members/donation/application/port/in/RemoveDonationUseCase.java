package page.clab.api.domain.members.donation.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveDonationUseCase {

    Long removeDonation(Long donationId) throws PermissionDeniedException;
}
