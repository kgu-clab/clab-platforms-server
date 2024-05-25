package page.clab.api.domain.activityGroup.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.GroupSchedule;
import page.clab.api.domain.activityGroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activityGroup.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupStatusResponseDto;
import page.clab.api.domain.activityGroup.dto.response.GroupMemberResponseDto;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/activity-group/member")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupMember", description = "활동 그룹 멤버")
@Slf4j
public class ActivityGroupMemberController {

    private final ActivityGroupMemberService activityGroupMemberService;

    @Operation(summary = "활동 전체 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<ActivityGroupResponseDto>> getActivityGroups(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException {
        List<String> sortByList = sortBy.orElse(List.of("createdAt"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("desc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, ActivityGroup.class);
        PagedResponseDto<ActivityGroupResponseDto> activityGroups = activityGroupMemberService.getActivityGroups(pageable);
        return ApiResponse.success(activityGroups);
    }

    @Operation(summary = "활동 상세 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/{activityGroupId}")
    public ApiResponse<Object> getActivityGroup(
            @PathVariable(name = "activityGroupId") Long activityGroupId
    ) {
        Object activityGroup = activityGroupMemberService.getActivityGroup(activityGroupId);
        return ApiResponse.success(activityGroup);
    }

    @Operation(summary = "[U] 나의 활동 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my")
    public ApiResponse<PagedResponseDto<ActivityGroupResponseDto>> getMyActivityGroups(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupResponseDto> activityGroups = activityGroupMemberService.getMyActivityGroups(pageable);
        return ApiResponse.success(activityGroups);
    }

    @Operation(summary = "[U] 활동 상태별 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/status")
    public ApiResponse<PagedResponseDto<ActivityGroupStatusResponseDto>> getActivityGroupsByStatus (
            @RequestParam(name = "activityGroupStatus") ActivityGroupStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupStatusResponseDto> activityGroups = activityGroupMemberService.getActivityGroupsByStatus(status, pageable);
        return ApiResponse.success(activityGroups);
    }

    @Operation(summary = "카테고리별 활동 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/list")
    public ApiResponse<PagedResponseDto<ActivityGroupResponseDto>> getActivityGroupsByCategory(
            @RequestParam(name = "category") ActivityGroupCategory category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException {
        List<String> sortByList = sortBy.orElse(List.of("createdAt"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("desc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, ActivityGroup.class);
        PagedResponseDto<ActivityGroupResponseDto> activityGroups = activityGroupMemberService.getActivityGroupsByCategory(category, pageable);
        return ApiResponse.success(activityGroups);
    }

    @Operation(summary = "[U] 활동 일정 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/schedule")
    public ApiResponse<PagedResponseDto<GroupScheduleDto>> getGroupScheduleList(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException {
        List<String> sortByList = sortBy.orElse(List.of("id"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("desc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, GroupSchedule.class);
        PagedResponseDto<GroupScheduleDto> groupSchedules = activityGroupMemberService.getGroupSchedules(activityGroupId, pageable);
        return ApiResponse.success(groupSchedules);
    }

    @Operation(summary = "[U] 활동 멤버 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "활동에 참여(수락)된 멤버만 조회 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/members")
    public ApiResponse<PagedResponseDto<GroupMemberResponseDto>> getActivityGroupMemberList(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException {
        List<String> sortByList = sortBy.orElse(List.of("memberId"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("asc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, GroupMember.class);
        PagedResponseDto<GroupMemberResponseDto> activityGroupMembers = activityGroupMemberService.getActivityGroupMembers(activityGroupId, pageable);
        return ApiResponse.success(activityGroupMembers);
    }

    @Operation(summary = "[U] 활동 신청", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/apply")
    public ApiResponse<Long> applyActivityGroup(
            @RequestParam Long activityGroupId,
            @Valid @RequestBody ApplyFormRequestDto requestDto
    ) {
        Long id = activityGroupMemberService.applyActivityGroup(activityGroupId, requestDto);
        return ApiResponse.success(id);
    }

}