package page.clab.api.domain.accuse.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.accuse.application.FetchMyAccusationsService;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.dto.response.AccuseMyResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accusations")
@RequiredArgsConstructor
@Tag(name = "Accusation", description = "신고")
public class FetchMyAccusationsController {

    private final FetchMyAccusationsService fetchMyAccusationsService;

    @Operation(summary = "[U] 나의 신고 내역 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, memberId, targetReferenceId")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my")
    public ApiResponse<PagedResponseDto<AccuseMyResponseDto>> fetchMyAccusations(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, Accuse.class);
        PagedResponseDto<AccuseMyResponseDto> accuses = fetchMyAccusationsService.execute(pageable);
        return ApiResponse.success(accuses);
    }
}