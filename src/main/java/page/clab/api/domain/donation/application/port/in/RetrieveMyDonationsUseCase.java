package page.clab.api.domain.donation.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMyDonationsUseCase {
    PagedResponseDto<DonationResponseDto> retrieve(Pageable pageable);
}
