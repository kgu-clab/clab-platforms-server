package page.clab.api.domain.donation.application;

import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateDonationService {
    Long execute(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException;
}