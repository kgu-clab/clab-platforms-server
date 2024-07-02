package page.clab.api.domain.donation.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DonationRemoveUseCase {
    Long remove(Long donationId) throws PermissionDeniedException;
}