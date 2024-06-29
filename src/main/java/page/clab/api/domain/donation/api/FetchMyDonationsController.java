package page.clab.api.domain.donation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.donation.application.FetchMyDonationsService;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Donation", description = "후원")
public class FetchMyDonationsController {

    private final FetchMyDonationsService fetchMyDonationsService;

    @Operation(summary = "[U] 나의 후원 정보", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, memberId")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my-donations")
    public ApiResponse<PagedResponseDto<DonationResponseDto>> getMyDonations(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, Donation.class);
        PagedResponseDto<DonationResponseDto> donations = fetchMyDonationsService.execute(pageable);
        return ApiResponse.success(donations);
    }
}
