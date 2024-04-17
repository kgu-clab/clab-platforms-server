package page.clab.api.domain.activityGroup.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityGroup.application.ActivityGroupAdminService;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;
import page.clab.api.domain.activityGroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupUpdateRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupMemberWithApplyReasonResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity-group/admin")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupAdmin", description = "활동 그룹 관리")
@Slf4j
public class ActivityGroupAdminController {

    private final ActivityGroupAdminService activityGroupAdminService;

    @Operation(summary = "[U] 활동 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createActivityGroup(
            @Valid @RequestBody ActivityGroupRequestDto requestDto
    ) {
        Long id = activityGroupAdminService.createActivityGroup(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 활동 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{activityGroupId}")
    public ApiResponse<Long> updateActivityGroup(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @Valid @RequestBody ActivityGroupUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.updateActivityGroup(activityGroupId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 활동 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("manage/{activityGroupId}")
    public ApiResponse<Long> manageActivityGroupStatus(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "activityGroupStatus") ActivityGroupStatus status
    ) {
        Long id = activityGroupAdminService.manageActivityGroup(activityGroupId, status);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 활동 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{activityGroupId}")
    public ApiResponse<Long> deleteActivityGroup(
            @PathVariable(name = "activityGroupId") Long activityGroupId
    ) {
        Long id = activityGroupAdminService.deleteActivityGroup(activityGroupId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 프로젝트 진행도 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "진행도는 0~100 사이의 값으로 입력해야 함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/progress/{activityGroupId}")
    public ApiResponse<Long> updateProjectProgress(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "progress") Long progress
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.updateProjectProgress(activityGroupId, progress);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 커리큘럼 및 일정 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/schedule")
    public ApiResponse<Long> addSchedule(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @Valid @RequestBody List<GroupScheduleDto> scheduleDtos
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.addSchedule(activityGroupId, scheduleDtos);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 활동 멤버 및 지원서 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "관리자 또는 리더만 조회 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/members")
    public ApiResponse<PagedResponseDto<ActivityGroupMemberWithApplyReasonResponseDto>> getApplyGroupMemberList(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) throws PermissionDeniedException {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupMemberWithApplyReasonResponseDto> groupMembers = activityGroupAdminService.getGroupMembersWithApplyReason(activityGroupId, pageable);
        return ApiResponse.success(groupMembers);
    }

    @Operation(summary = "[U] 신청 멤버 상태 변경", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/accept")
    public ApiResponse<String> acceptGroupMember(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "status") GroupMemberStatus status
    ) throws PermissionDeniedException {
        String id = activityGroupAdminService.manageGroupMemberStatus(activityGroupId, memberId, status);
        return ApiResponse.success(id);
    }

}
