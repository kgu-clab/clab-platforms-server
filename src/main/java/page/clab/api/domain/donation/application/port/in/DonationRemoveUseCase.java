package page.clab.api.domain.donation.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DonationRemoveUseCase {
    Long remove(Long donationId) throws PermissionDeniedException;
}