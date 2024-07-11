package page.clab.api.domain.accuse.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.accuse.application.dto.response.AccuseMyResponseDto;
import page.clab.api.domain.accuse.application.port.in.RetrieveMyAccusationsUseCase;
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
public class MyAccusationsController {

    private final RetrieveMyAccusationsUseCase retrieveMyAccusationsUsecase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 나의 신고 내역 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @GetMapping("/my")
    public ApiResponse<PagedResponseDto<AccuseMyResponseDto>> retrieveMyAccusations(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, AccuseMyResponseDto.class);
        PagedResponseDto<AccuseMyResponseDto> accuses = retrieveMyAccusationsUsecase.retrieveMyAccusations(pageable);
        return ApiResponse.success(accuses);
    }
}
