package page.clab.api.domain.sharedAccount.api;

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
import page.clab.api.domain.sharedAccount.application.SharedAccountService;
import page.clab.api.domain.sharedAccount.application.SharedAccountUsageService;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsageStatus;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountRequestDto;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUpdateRequestDto;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUsageRequestDto;
import page.clab.api.domain.sharedAccount.dto.response.SharedAccountResponseDto;
import page.clab.api.domain.sharedAccount.dto.response.SharedAccountUsageResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/shared-accounts")
@RequiredArgsConstructor
@Tag(name = "SharedAccount", description = "공동 계정")
@Slf4j
public class SharedAccountController {

    private final SharedAccountService sharedAccountService;

    private final SharedAccountUsageService sharedAccountUsageService;

    @Operation(summary = "[S] 공동 계정 추가", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createSharedAccount(
            @Valid @RequestBody SharedAccountRequestDto requestDto
    ) {
        Long id = sharedAccountService.createSharedAccount(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 공동 계정 조회(상태 포함)", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<SharedAccountResponseDto>> getSharedAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, SharedAccount.class);
        PagedResponseDto<SharedAccountResponseDto> sharedAccounts = sharedAccountService.getSharedAccounts(pageable);
        return ApiResponse.success(sharedAccounts);
    }

    @Operation(summary = "[S] 공동 계정 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{accountId}")
    public ApiResponse<Long> updateSharedAccount(
            @PathVariable(name = "accountId") Long accountId,
            @Valid @RequestBody SharedAccountUpdateRequestDto requestDto
    ) {
        Long id = sharedAccountService.updateSharedAccount(accountId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[S] 공동 계정 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{accountId}")
    public ApiResponse<Long> deleteSharedAccount(
            @PathVariable(name = "accountId") Long accountId
    ) {
        Long id = sharedAccountService.deleteSharedAccount(accountId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 공동 계정 이용 신청", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "이미 사용 또는 예약으로 등록된 시간대는 신청 불가능<br>" +
            "신청시에 계정 상태가 자동으로 바뀌므로 상태 변경은 별도로 안해도 됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/usage")
    public ApiResponse<Long> requestSharedAccountUsage(
            @Valid @RequestBody SharedAccountUsageRequestDto requestDto
    ) throws CustomOptimisticLockingFailureException {
        Long id = sharedAccountUsageService.requestSharedAccountUsage(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 공동 계정 이용 내역 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/usage")
    public ApiResponse<PagedResponseDto<SharedAccountUsageResponseDto>> getSharedAccountUsages(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, SharedAccount.class);
        PagedResponseDto<SharedAccountUsageResponseDto> sharedAccountUsages = sharedAccountUsageService.getSharedAccountUsages(pageable);
        return ApiResponse.success(sharedAccountUsages);
    }

    @Operation(summary = "[U] 공동 계정 이용 상태 변경", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "이용 중 취소/완료, 예약 취소만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/usage/{usageId}")
    public ApiResponse<Long> updateSharedAccountUsage(
            @PathVariable(name = "usageId") Long usageId,
            @RequestParam(name = "status") SharedAccountUsageStatus status
    ) throws PermissionDeniedException {
        Long id = sharedAccountUsageService.updateSharedAccountUsage(usageId, status);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 공동 계정 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<SharedAccountResponseDto>> getDeletedSharedAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<SharedAccountResponseDto> sharedAccounts = sharedAccountService.getDeletedSharedAccounts(pageable);
        return ApiResponse.success(sharedAccounts);
    }

}