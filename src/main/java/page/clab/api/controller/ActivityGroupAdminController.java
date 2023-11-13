package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.ActivityGroupAdminService;
import page.clab.api.type.dto.ActivityGroupDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.etc.ActivityGroupStatus;

@RestController
@RequestMapping("/activity-group/admin")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupAdmin", description = "활동 그룹 관리 API")
@Slf4j
public class ActivityGroupAdminController {

    private final ActivityGroupAdminService activityGroupAdminService;

    @Operation(summary = "[U] 활동 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createActivityGroup(
            @Valid @RequestBody ActivityGroupDto activityGroupDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        ResponseModel responseModel = ResponseModel.builder().build();
        activityGroupAdminService.createActivityGroup(activityGroupDto);
        return responseModel;
    }

    @Operation(summary = "[A] 활동 상태별 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getActivityGroupsByStatus (
            @RequestParam ActivityGroupStatus activityGroupStatus
    ) throws PermissionDeniedException {
        List<ActivityGroupDto> waitingGroups = activityGroupAdminService.getActivityGroupsByStatus(activityGroupStatus);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(waitingGroups);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PatchMapping("/{activityGroupId}")
    public ResponseModel updateActivityGroup(
            @PathVariable Long activityGroupId,
            @Valid @RequestBody ActivityGroupDto activityGroupDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        activityGroupAdminService.updateActivityGroup(activityGroupId, activityGroupDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 활동 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PatchMapping("manage/{activityGroupId}")
    public ResponseModel manageActivityGroupStatus(
            @PathVariable Long activityGroupId,
            @RequestParam ActivityGroupStatus activityGroupStatus
    ) throws PermissionDeniedException {
        activityGroupAdminService.manageActivityGroup(activityGroupId, activityGroupStatus);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 활동 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("/{activityGroupId}")
    public ResponseModel deleteActivityGroup(
            @PathVariable Long activityGroupId
    ) throws PermissionDeniedException {
        activityGroupAdminService.deleteActivityGroup(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 프로젝트 진행도 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "진행도는 0~100 사이의 값으로 입력해야 함")
    @PatchMapping("/progress/{activityGroupId}")
    public ResponseModel updateProjectProgress(
            @PathVariable Long activityGroupId,
            @RequestParam Long progress
    ) throws PermissionDeniedException {
        activityGroupAdminService.updateProjectProgress(activityGroupId, progress);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 커리큘럼 및 일정 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PatchMapping("/schedule")
    public ResponseModel addSchedule(
            @RequestParam Long activityGroupId,
            @Valid @RequestBody List<GroupScheduleDto> groupScheduleDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        activityGroupAdminService.addSchedule(activityGroupId, groupScheduleDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 인증 코드 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/auth")
    public ResponseModel createMemberAuthCode(
            @RequestParam Long activityGroupId,
            @RequestParam String code
    ) throws PermissionDeniedException {
        activityGroupAdminService.createMemberAuthCode(activityGroupId, code);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
