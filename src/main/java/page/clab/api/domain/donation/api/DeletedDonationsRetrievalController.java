package page.clab.api.domain.donation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.donation.application.port.in.RetrieveDeletedDonationsUseCase;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Donation", description = "후원")
public class DeletedDonationsRetrievalController {

    private final RetrieveDeletedDonationsUseCase retrieveDeletedDonationsUseCase;

    @GetMapping("/deleted")
    @Secured({ "ROLE_SUPER" })
    @Operation(summary = "[S] 삭제된 후원 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    public ApiResponse<PagedResponseDto<DonationResponseDto>> retrieveDeletedDonations(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<DonationResponseDto> donations = retrieveDeletedDonationsUseCase.retrieveDeletedDonations(pageable);
        return ApiResponse.success(donations);
    }
}
