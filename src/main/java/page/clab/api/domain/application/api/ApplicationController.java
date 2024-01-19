package page.clab.api.domain.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
@Tag(name = "Application", description = "동아리 가입 신청 관련 API")
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "가입 신청", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createApplication(
            @Valid @RequestBody ApplicationRequestDto applicationRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        String id = applicationService.createApplication(applicationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 신청자 목록 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ApplicationResponseDto> applications = applicationService.getApplications(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applications);
        return responseModel;
    }

    @Operation(summary = "[A] 신청자 목록 필터링(업데이트 날짜 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "전달된 날짜 사이의 신청자를 필터링하여 반환")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/filter")
    public ResponseModel getApplicationsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ApplicationResponseDto> applications = applicationService.getApplicationsBetweenDates(startDate, endDate, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applications);
        return responseModel;
    }

    @Operation(summary = "[A] 신청자 검색", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "신청자의 학번을 기반으로 검색")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/search")
    public ResponseModel searchApplication(
            @RequestParam String applicationId
    ) {
        ApplicationResponseDto application = applicationService.searchApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(application);
        return responseModel;
    }

    @Operation(summary = "[A] 가입 신청 승인/취소", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "승인/취소 상태가 반전됨")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{applicationId}")
    public ResponseModel approveApplication(
            @PathVariable String applicationId
    ) {
        String id = applicationService.approveApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 합격자 목록 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/pass")
    public ResponseModel getApprovedApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ApplicationResponseDto> approvedApplications = applicationService.getApprovedApplications(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(approvedApplications);
        return responseModel;
    }

    @Operation(summary = "합격 여부 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/{applicationId}")
    public ResponseModel getApplicationPass(
            @PathVariable String applicationId
    ) {
        ApplicationPassResponseDto applicationPassResponseDto = applicationService.getApplicationPass(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applicationPassResponseDto);
        return responseModel;
    }

    @Operation(summary = "[A] 가입 신청서 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{applicationId}")
    public ResponseModel deleteApplication(
            @PathVariable String applicationId
    ) {
        String id = applicationService.deleteApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
