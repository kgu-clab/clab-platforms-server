package page.clab.api.domain.hiring.recruitment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecentRecruitmentsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Hiring - Recruitment", description = "모집 공고")
public class RecentRecruitmentsRetrievalController {

    private final RetrieveRecentRecruitmentsUseCase retrieveRecentRecruitmentsUseCase;

    @Operation(summary = "모집 공고 목록(최근 5건)", description = "ROLE_ANONYMOUS 이상의 권한이 필요함<br>" +
            "최근 5건의 모집 공고를 조회")
    @GetMapping("")
    public ApiResponse<List<RecruitmentResponseDto>> retrieveRecentRecruitments() {
        List<RecruitmentResponseDto> recruitments = retrieveRecentRecruitmentsUseCase.retrieveRecentRecruitments();
        return ApiResponse.success(recruitments);
    }
}
