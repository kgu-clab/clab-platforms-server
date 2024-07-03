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
import page.clab.api.domain.donation.application.port.in.RetrieveDonationsByConditionsUseCase;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Donation", description = "후원")
public class DonationsByConditionsRetrievalController {

    private final RetrieveDonationsByConditionsUseCase retrieveDonationsByConditionsUseCase;

    @Operation(summary = "[U] 후원 목록 조회(멤버 ID, 멤버 이름, 기간 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "멤버 ID, 멤버 이름, 기간 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, memberId")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<DonationResponseDto>> retrieveDonationsByConditions(
            @RequestParam(name = "memberId", required = false) String memberId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, Donation.class);
        PagedResponseDto<DonationResponseDto> donations = retrieveDonationsByConditionsUseCase.retrieve(memberId, name, startDate, endDate, pageable);
        return ApiResponse.success(donations);
    }
}