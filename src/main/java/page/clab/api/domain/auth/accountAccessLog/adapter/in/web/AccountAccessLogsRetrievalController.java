package page.clab.api.domain.auth.accountAccessLog.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.accountAccessLog.application.dto.response.AccountAccessLogResponseDto;
import page.clab.api.domain.auth.accountAccessLog.application.port.in.RetrieveAccountAccessLogsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account-access-logs")
@RequiredArgsConstructor
@Tag(name = "Authentication - Account Access Log", description = "계정 접근 로그")
public class AccountAccessLogsRetrievalController {

    private final RetrieveAccountAccessLogsUseCase retrieveAccountAccessLogsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[S] 계정별 접근 로그 조회", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @Secured({ "ROLE_SUPER" })
    @GetMapping("/{memberId}")
    public ApiResponse<PagedResponseDto<AccountAccessLogResponseDto>> retrieveAccountAccessLogs(
            @PathVariable(name = "memberId") String memberId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "accessTime") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, AccountAccessLogResponseDto.class);
        PagedResponseDto<AccountAccessLogResponseDto> accountAccessLogs = retrieveAccountAccessLogsUseCase.retrieveAccountAccessLogs(memberId, pageable);
        return ApiResponse.success(accountAccessLogs);
    }
}