package page.clab.api.domain.hiring.recruitment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentDetailsResponseDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecruitmentUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Hiring - Recruitment", description = "모집 공고")
public class RecruitmentDetailsRetrievalController {

    private final RetrieveRecruitmentUseCase retrieveRecruitmentUseCase;

    @Operation(summary = "모집 공고 상세 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/{recruitmentId}")
    public ApiResponse<RecruitmentDetailsResponseDto> retrieveRecruitmentDetails(
        @PathVariable(name = "recruitmentId") Long recruitmentId
    ) {
        RecruitmentDetailsResponseDto recruitment = retrieveRecruitmentUseCase.retrieveRecruitmentDetails(recruitmentId);
        return ApiResponse.success(recruitment);
    }
}
