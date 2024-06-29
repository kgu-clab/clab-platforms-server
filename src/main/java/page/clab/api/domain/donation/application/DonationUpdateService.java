package page.clab.api.domain.donation.application;

import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface DonationUpdateService {
    Long update(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException;
}