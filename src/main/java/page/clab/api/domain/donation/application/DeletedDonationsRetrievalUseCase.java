package page.clab.api.domain.donation.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedDonationsRetrievalUseCase {
    PagedResponseDto<DonationResponseDto> retrieve(Pageable pageable);
}
