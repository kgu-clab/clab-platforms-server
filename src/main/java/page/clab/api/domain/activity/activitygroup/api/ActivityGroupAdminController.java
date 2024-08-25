package page.clab.api.domain.activity.activitygroup.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupAdminService;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;
import page.clab.api.domain.activity.activitygroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupUpdateRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardStatusUpdatedResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupMemberWithApplyReasonResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity-group/admin")
@RequiredArgsConstructor
@Tag(name = "Activity - Group Admin", description = "활동 그룹 관리")
public class ActivityGroupAdminController {

    private final ActivityGroupAdminService activityGroupAdminService;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 활동 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ApiResponse<Long> createActivityGroup(
            @Valid @RequestBody ActivityGroupRequestDto requestDto
    ) {
        Long id = activityGroupAdminService.createActivityGroup(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 활동 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{activityGroupId}")
    public ApiResponse<Long> updateActivityGroup(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @Valid @RequestBody ActivityGroupUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.updateActivityGroup(activityGroupId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 활동 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("manage/{activityGroupId}")
    public ApiResponse<ActivityGroupBoardStatusUpdatedResponseDto> manageActivityGroupStatus(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "activityGroupStatus") ActivityGroupStatus status
    ) {
        ActivityGroupBoardStatusUpdatedResponseDto updatedStatusDto = activityGroupAdminService.manageActivityGroup(activityGroupId, status);
        return ApiResponse.success(updatedStatusDto);
    }

    @Operation(summary = "[A] 활동 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{activityGroupId}")
    public ApiResponse<Long> deleteActivityGroup(
            @PathVariable(name = "activityGroupId") Long activityGroupId
    ) {
        Long id = activityGroupAdminService.deleteActivityGroup(activityGroupId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 프로젝트 진행도 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "진행도는 0~100 사이의 값으로 입력해야 함")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/progress/{activityGroupId}")
    public ApiResponse<Long> updateProjectProgress(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "progress") Long progress
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.updateProjectProgress(activityGroupId, progress);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 커리큘럼 및 일정 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/schedule")
    public ApiResponse<Long> addSchedule(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @Valid @RequestBody List<GroupScheduleDto> scheduleDtos
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.addSchedule(activityGroupId, scheduleDtos);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 활동 멤버 및 지원서 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "관리자 또는 리더만 조회 가능<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/members")
    public ApiResponse<PagedResponseDto<ActivityGroupMemberWithApplyReasonResponseDto>> getApplyGroupMemberList(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "memberId") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws SortingArgumentException, PermissionDeniedException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, ActivityGroupMemberWithApplyReasonResponseDto.class);
        PagedResponseDto<ActivityGroupMemberWithApplyReasonResponseDto> groupMembers = activityGroupAdminService.getGroupMembersWithApplyReason(activityGroupId, pageable);
        return ApiResponse.success(groupMembers);
    }

    @Operation(summary = "[U] 신청 멤버 상태 변경", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/accept")
    public ApiResponse<Long> acceptGroupMember(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "status") GroupMemberStatus status
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.manageGroupMemberStatus(activityGroupId, memberId, status);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 활동 멤버 직책 변경", description = "ROLE_USER 이상의 권한이 필요함<br><br>" +
            "직책은 팀장만 변경 가능<br>" +
            "LEADER: 팀장, MEMBER: 팀원, NONE: 없음<br>" +
            "LEADER -> MEMBER, MEMBER -> LEADER 변경만 허용함")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/position")
    public ApiResponse<Long> changeGroupMemberPosition(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "position") ActivityGroupRole position
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.changeGroupMemberPosition(activityGroupId, memberId, position);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 활동그룹 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponse<PagedResponseDto<ActivityGroupResponseDto>> getDeletedActivityGroups(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupResponseDto> activityGroups = activityGroupAdminService.getDeletedActivityGroups(pageable);
        return ApiResponse.success(activityGroups);
    }
}
