package page.clab.api.domain.members.donation.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.donation.application.dto.response.DonationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMyDonationsUseCase {
    PagedResponseDto<DonationResponseDto> retrieveMyDonations(Pageable pageable);
}
