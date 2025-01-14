package page.clab.api.domain.hiring.recruitment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentOpenResponseDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveOpenRecruitmentsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Hiring - Recruitment", description = "모집 공고")
public class OpenRecruitmentsRetrievalController {

    private final RetrieveOpenRecruitmentsUseCase retrieveOpenRecruitmentsUseCase;

    @Operation(summary = "현재 모집 중인 모집 공고 목록", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/open")
    public ApiResponse<List<RecruitmentOpenResponseDto>> retrieveOpenRecruitments() {
        List<RecruitmentOpenResponseDto> recruitments = retrieveOpenRecruitmentsUseCase.retrieveOpenRecruitments();
        return ApiResponse.success(recruitments);
    }
}
