package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.ApplicationService;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ApplicationResponseDto;
import page.clab.api.type.dto.ResponseModel;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Tag(name = "Application")
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "동아리 가입 신청", description = "동아리 가입 신청<br>" +
            "ApplicationType: NORMAL / CORE_TEAM")
    @PostMapping("")
    public ResponseModel createApplication (
            @RequestBody ApplicationRequestDto applicationRequestDto
    ) {
        applicationService.createApplication(applicationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청자 전체 목록", description = "동아리 가입 신청자 전체 목록")
    @GetMapping("")
    public ResponseModel getApplications() throws PermissionDeniedException {
        List<ApplicationResponseDto> applications = applicationService.getApplications();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applications);
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청자 목록 필터링(업데이트 날짜 기준)", description = "전달된 날짜 사이의 신청자를 필터링함")
    @GetMapping("/list")
    public ResponseModel getApplicationsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws PermissionDeniedException {
        List<ApplicationResponseDto> applications = applicationService.getApplicationsBetweenDates(startDate, endDate);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applications);
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청자 검색", description = "신청자의 학번을 기반으로 검색")
    @GetMapping("/search")
    public ResponseModel searchApplication(
            @RequestParam String applicationId
    ) {
        ApplicationResponseDto application = applicationService.searchApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(application);
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청 승인/취소", description = "동아리 가입 신청 승인/취소<br>" +
        "승인/취소 상태가 반전됨")
    @PostMapping("/approve/{applicationId}")
    public ResponseModel approveApplication(
            @PathVariable String applicationId
    ) throws PermissionDeniedException {
        applicationService.approveApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "동아리 합격자 목록", description = "동아리 합격자 목록을 조회합니다.")
    @GetMapping("/pass")
    public ResponseModel getApprovedApplications() throws PermissionDeniedException {
        List<ApplicationResponseDto> approvedApplications = applicationService.getApprovedApplications();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(approvedApplications);
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청서 삭제", description = "동아리 가입 신청서 삭제")
    @DeleteMapping("/{applicationId}")
    public ResponseModel deleteApplication(
            @PathVariable String applicationId
    ) throws PermissionDeniedException {
        applicationService.deleteApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
