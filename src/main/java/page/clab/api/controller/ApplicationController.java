package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import page.clab.api.service.ApplicationService;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.entity.Application;

import java.util.List;

@RestController
@RequestMapping("/application")
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
    public ResponseModel getAllApplication() {
//        String userId = AuthUtil.getAuthenticationInfoUserId();
        String userId = "201912156"; // 임시 테스트용
        List<Application> applications = applicationService.getAllApplication(userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applications);
        return responseModel;
    }

    @Operation(summary = "동아리 가입 신청자 정보 상세보기", description = "동아리 가입 신청자 정보 상세보기")
    @GetMapping("/list/{applicationId}")
    public ResponseModel getApplication(
            @PathVariable String applicationId
    ) {
//        String userId = AuthUtil.getAuthenticationInfoUserId();
        String userId = "201912156"; // 임시 테스트용
        Application application = applicationService.getApplication(applicationId, userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(application);
        return responseModel;
    }

}
