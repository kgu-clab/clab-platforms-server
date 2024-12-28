package page.clab.api.domain.hiring.recruitment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentEndDateResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecentRecruitmentsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

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

    @Operation(summary = "모집 종료 기간 기준 최신 일주일 모집 공고 목록", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/recent-week")
    public ApiResponse<List<RecruitmentEndDateResponseDto>> retrieveRecruitmentsByEndDate() {
        List<RecruitmentEndDateResponseDto> recruitments = retrieveRecentRecruitmentsUseCase.retrieveRecruitmentsByEndDate();
        return ApiResponse.success(recruitments);
    }
}
