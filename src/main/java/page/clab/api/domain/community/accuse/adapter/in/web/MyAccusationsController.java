package page.clab.api.domain.community.accuse.adapter.in.web;

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
import page.clab.api.domain.community.accuse.application.dto.response.AccuseMyResponseDto;
import page.clab.api.domain.community.accuse.application.port.in.RetrieveMyAccusationsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/accusations")
@RequiredArgsConstructor
@Tag(name = "Community - Accusation", description = "커뮤니티 신고")
public class MyAccusationsController {

    private final RetrieveMyAccusationsUseCase retrieveMyAccusationsUsecase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[G] 나의 신고 내역 조회", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/my")
    public ApiResponse<PagedResponseDto<AccuseMyResponseDto>> retrieveMyAccusations(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, AccuseMyResponseDto.class);
        PagedResponseDto<AccuseMyResponseDto> accuses = retrieveMyAccusationsUsecase.retrieveMyAccusations(pageable);
        return ApiResponse.success(accuses);
    }
}
