package page.clab.api.domain.donation.application.port.in;

import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface DonationUpdateUseCase {
    Long update(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException;
}