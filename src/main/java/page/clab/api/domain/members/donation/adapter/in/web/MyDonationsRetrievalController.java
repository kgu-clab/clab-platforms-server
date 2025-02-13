package page.clab.api.domain.members.donation.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.donation.application.dto.response.DonationResponseDto;
import page.clab.api.domain.members.donation.application.port.in.RetrieveMyDonationsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Members - Donation", description = "후원")
public class MyDonationsRetrievalController {

    private final RetrieveMyDonationsUseCase retrieveMyDonationsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[G] 나의 후원 정보", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/my-donations")
    public ApiResponse<PagedResponseDto<DonationResponseDto>> retrieveMyDonations(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, DonationResponseDto.class);
        PagedResponseDto<DonationResponseDto> donations = retrieveMyDonationsUseCase.retrieveMyDonations(pageable);
        return ApiResponse.success(donations);
    }
}
