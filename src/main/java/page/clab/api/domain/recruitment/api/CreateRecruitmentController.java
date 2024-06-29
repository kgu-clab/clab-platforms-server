package page.clab.api.domain.recruitment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.recruitment.application.CreateRecruitmentService;
import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment", description = "모집 공고")
public class CreateRecruitmentController {

    private final CreateRecruitmentService createRecruitmentService;

    @Operation(summary = "[S] 모집 공고 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createRecruitment(
            @Valid @RequestBody RecruitmentRequestDto requestDto
    ) {
        Long id = createRecruitmentService.execute(requestDto);
        return ApiResponse.success(id);
    }
}
