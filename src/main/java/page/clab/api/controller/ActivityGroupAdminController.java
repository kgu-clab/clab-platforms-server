package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.ActivityGroupAdminService;
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.ActivityGroupDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.dto.ResponseModel;

import java.util.List;

@RestController
@RequestMapping("/activity-group/admin")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupAdmin")
@Slf4j
public class ActivityGroupAdminController {

    private final ActivityGroupAdminService activityGroupAdminService;

    private final MemberService memberService;

    // 완료
    @Operation(summary = "승인 대기 활동 조회", description = "승인 대기 활동 조회")
    @GetMapping("")
    public ResponseModel getWaitingActivityGroup (
    ) throws PermissionDeniedException {
        ResponseModel responseModel = ResponseModel.builder().build();
        List<ActivityGroupDto> waitingGroups = activityGroupAdminService.getWaitingActivityGroup();
        responseModel.setData(waitingGroups);
        return responseModel;
    }

    // 완료
    @Operation(summary = "활동 승인", description = "활동 승인<br>")
    @PostMapping("/{ActivityGroupId}/approve")
    public ResponseModel approveActivityGroup(
            @PathVariable Long ActivityGroupId
    ) throws PermissionDeniedException {
        ResponseModel responseModel = ResponseModel.builder().build();
        activityGroupAdminService.approveActivityGroup(ActivityGroupId);
        return responseModel;
    }

    // 완료
    @Operation(summary = "활동 완료 상태 변경", description = "활동 완료 상태 변경")
    @PatchMapping("/{ActivityGroupId}/complete")
    public ResponseModel completeActivityGroup(
            @PathVariable Long ActivityGroupId
    ) throws PermissionDeniedException {
        activityGroupAdminService.completeActivityGroup(ActivityGroupId);
        return ResponseModel.builder().build();
    }

    // 완료
    @Operation(summary = "활동 생성", description = "활동 생성<br>" +
            "String category; <br>" +
            "String name; <br>" +
            "String content; <br>" +
            "String imageUrl;")
    @PostMapping("")
    public ResponseModel createActivityGroup(
            @RequestBody ActivityGroupDto activityGroupDto
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        activityGroupAdminService.createActivityGroup(activityGroupDto);
        return responseModel;
    }

    // 완료
    @Operation(summary = "활동 수정")
    @PatchMapping("")
    public ResponseModel updateActivityGroup(
            @RequestParam Long id,
            @RequestBody ActivityGroupDto activityGroupDto
    ) throws PermissionDeniedException {
        ResponseModel responseModel = ResponseModel.builder().build();
        activityGroupAdminService.updateActivityGroup(id, activityGroupDto);
        return responseModel;
    }

//    @Operation(summary = "활동 삭제")
//    @DeleteMapping("")
//    public ResponseModel deleteActivityGroup(
//            @RequestParam Long id
//    ) throws PermissionDeniedException {
//        ResponseModel responseModel = ResponseModel.builder().build();
//        activityGroupAdminService.deleteActivityGroup(id);
//        return responseModel;
//    }

    // 완료
    @Operation(summary = "프로젝트 진행도 수정")
    @PatchMapping("/{id}/progress")
    public ResponseModel updateProjectProgress(
            @PathVariable Long id,
            @RequestParam Long progress
    ) throws PermissionDeniedException{
        ResponseModel responseModel = ResponseModel.builder().build();
        activityGroupAdminService.updateProjectProgress(id, progress);
        return responseModel;
    }

    // 완료
    @Operation(summary = "커리큘럼 및 일정 생성")
    @PatchMapping("/schedule")
    public ResponseModel addSchedule(
            @RequestParam Long id,
            @RequestBody List<GroupScheduleDto> groupScheduleDto
    ) throws PermissionDeniedException {
        ResponseModel responseModel = ResponseModel.builder().build();
        activityGroupAdminService.addSchedule(id, groupScheduleDto);
        return responseModel;
    }

    @Operation(summary = "멤버 인증 코드 생성")
    @PostMapping("/auth")
    public ResponseModel createMemberAuthCode(
            @RequestParam Long id,
            @RequestParam String code
    ) throws PermissionDeniedException {
        ResponseModel responseModel = ResponseModel.builder().build();
        activityGroupAdminService.createMemberAuthCode(id, code);
        return responseModel;
    }


}
