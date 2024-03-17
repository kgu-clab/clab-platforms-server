package page.clab.api.domain.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.application.application.ApplicationService;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Tag(name = "Application", description = "동아리 지원")
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "동아리 지원", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createApplication(
            HttpServletRequest request,
            @Valid @RequestBody ApplicationRequestDto applicationRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        String id = applicationService.createApplication(request, applicationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 지원자 목록 조회(모집 일정 ID, 지원자 ID, 합격 여부 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "모집 일정 ID, 지원자 ID, 합격 여부 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/conditions")
    public ResponseModel getApplicationsByConditions(
            @RequestParam(name = "recruitmentId", required = false) Long recruitmentId,
            @RequestParam(name = "studentId", required = false) String studentId,
            @RequestParam(name = "isPass", required = false) Boolean isPass,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ApplicationResponseDto> applications = applicationService.getApplicationsByConditions(recruitmentId, studentId, isPass, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applications);
        return responseModel;
    }

    @Operation(summary = "[S] 지원 합격/취소", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "승인/취소 상태가 반전됨")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{recruitmentId}/{studentId}")
    public ResponseModel toggleApprovalStatus(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
            ) {
        String id = applicationService.toggleApprovalStatus(recruitmentId, studentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "합격 여부 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/{recruitmentId}/{studentId}")
    public ResponseModel getApplicationPass(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
    ) {
        ApplicationPassResponseDto applicationPassResponseDto = applicationService.getApplicationPass(recruitmentId, studentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applicationPassResponseDto);
        return responseModel;
    }

    @Operation(summary = "[S] 지원서 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{recruitmentId}/{studentId}")
    public ResponseModel deleteApplication(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
    ) {
        String id = applicationService.deleteApplication(recruitmentId, studentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
