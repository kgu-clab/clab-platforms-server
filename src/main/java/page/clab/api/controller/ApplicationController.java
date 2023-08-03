package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
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
            "ApplicationRequestDto<br>" +
            "String studentId;<br>" +
            "String name;<br>" +
            "String contact;<br>" +
            "String email;<br>" +
            "String department;<br>" +
            "Long grade;<br>" +
            "LocalDate birth;<br>" +
            "String address;<br>" +
            "String interests;<br>" +
            "String otherActivities;")
    @PostMapping("/create")
    public ResponseModel createApplication (
            @RequestBody ApplicationRequestDto applicationRequestDto
    ) {
        applicationService.createApplication(applicationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청자 전체 목록", description = "동아리 가입 신청자 전체 목록")
    @GetMapping("/list")
    public ResponseModel getApplications() throws PermissionDeniedException {
        List<ApplicationResponseDto> applications = applicationService.getApplications();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applications);
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청자 목록 필터링(날짜 기준)", description = "전달된 날짜 사이의 신청자를 필터링함")
    @PostMapping("/list")
    public ResponseModel getApplicationsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws PermissionDeniedException {
        List<ApplicationResponseDto> applications = applicationService.getApplicationsBetweenDates(startDate, endDate);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applications);
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청자 검색", description = "신청자의 학번 또는 이름을 기반으로 검색")
    @GetMapping("/search")
    public ResponseModel searchApplication(
            @RequestParam(required = false) String applicationId,
            @RequestParam(required = false) String name
    ) throws PermissionDeniedException {
        ApplicationResponseDto application = applicationService.searchApplication(applicationId, name);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(application);
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청 승인", description = "동아리 가입 신청 승인")
    @PostMapping("/approve/{applicationId}")
    public ResponseModel approveApplication(
            @PathVariable String applicationId
    ) throws PermissionDeniedException {
        applicationService.approveApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "동아리 가입 승인 취소", description = "동아리 가입 신청 취소 (24시간 내에만 가능)")
    @DeleteMapping("/cancel/{applicationId}")
    public ResponseModel cancelApplication(
            @PathVariable String applicationId
    ) throws PermissionDeniedException {
        applicationService.cancelApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청서 삭제", description = "동아리 가입 신청서 삭제")
    @DeleteMapping("/delete/{applicationId}")
    public ResponseModel deleteApplication(
            @PathVariable String applicationId
    ) throws PermissionDeniedException {
        applicationService.deleteApplication(applicationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "동아리 가입 합격자 목록", description = "동아리 가입 합격자 목록을 조회합니다.")
    @GetMapping("/pass")
    public ResponseModel getApprovedApplications() throws PermissionDeniedException {
        List<ApplicationResponseDto> approvedApplications = applicationService.getApprovedApplications();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(approvedApplications);
        return responseModel;
    }

}
