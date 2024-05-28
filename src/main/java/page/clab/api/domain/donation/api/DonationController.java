package page.clab.api.domain.donation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.donation.application.DonationService;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Donation", description = "후원")
@Slf4j
public class DonationController {

    private final DonationService donationService;

    @Operation(summary = "[S] 후원 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createDonation(
            @Valid @RequestBody DonationRequestDto requestDto
    ) {
        Long id = donationService.createDonation(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 후원 목록 조회(멤버 ID, 멤버 이름, 기간 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "멤버 ID, 멤버 이름, 기간 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, memberId")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<DonationResponseDto>> getDonationsByConditions(
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
        PagedResponseDto<DonationResponseDto> donations = donationService.getDonationsByConditions(memberId, name, startDate, endDate, pageable);
        return ApiResponse.success(donations);
    }

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
        PagedResponseDto<DonationResponseDto> donations = donationService.getMyDonations(pageable);
        return ApiResponse.success(donations);
    }

    @Operation(summary = "[S] 후원 정보 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{donationId}")
    public ApiResponse<Long> updateDonation(
            @PathVariable(name = "donationId") Long donationId,
            @Valid @RequestBody DonationUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = donationService.updateDonation(donationId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[S] 후원 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{donationId}")
    public ApiResponse<Long> deleteDonation(
            @PathVariable(name = "donationId") Long donationId
    ) throws PermissionDeniedException {
        Long id = donationService.deleteDonation(donationId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 후원 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<DonationResponseDto>> getDeletedDonations(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<DonationResponseDto> donations = donationService.getDeletedDonations(pageable);
        return ApiResponse.success(donations);
    }

}