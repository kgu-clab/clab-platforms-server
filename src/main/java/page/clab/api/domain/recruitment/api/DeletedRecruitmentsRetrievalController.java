package page.clab.api.domain.recruitment.api;

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
import page.clab.api.domain.recruitment.application.DeletedRecruitmentsRetrievalService;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment", description = "모집 공고")
public class DeletedRecruitmentsRetrievalController {

    private final DeletedRecruitmentsRetrievalService deletedRecruitmentsRetrievalService;

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 모집 공고 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<RecruitmentResponseDto>> retrieveDeletedRecruitments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<RecruitmentResponseDto> recruitments = deletedRecruitmentsRetrievalService.retrieve(pageable);
        return ApiResponse.success(recruitments);
    }
}
