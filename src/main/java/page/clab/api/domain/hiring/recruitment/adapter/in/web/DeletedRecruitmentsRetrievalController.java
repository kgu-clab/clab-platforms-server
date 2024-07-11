package page.clab.api.domain.hiring.recruitment.adapter.in.web;

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
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveDeletedRecruitmentsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Hiring - Recruitment", description = "모집 공고 관련 API")
public class DeletedRecruitmentsRetrievalController {

    private final RetrieveDeletedRecruitmentsUseCase retrieveDeletedRecruitmentsUseCase;

    @GetMapping("/deleted")
    @Secured({ "ROLE_SUPER" })
    @Operation(summary = "[S] 삭제된 모집 공고 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    public ApiResponse<PagedResponseDto<RecruitmentResponseDto>> retrieveDeletedRecruitments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<RecruitmentResponseDto> recruitments =
                retrieveDeletedRecruitmentsUseCase.retrieveDeletedRecruitments(pageable);
        return ApiResponse.success(recruitments);
    }
}
