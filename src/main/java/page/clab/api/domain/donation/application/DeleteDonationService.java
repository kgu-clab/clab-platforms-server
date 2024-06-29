package page.clab.api.domain.donation.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteDonationService {
    Long execute(Long donationId) throws PermissionDeniedException;
}