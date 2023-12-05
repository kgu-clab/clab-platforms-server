package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.ActivityGroupMemberService;
import page.clab.api.type.dto.ActivityGroupProjectResponseDto;
import page.clab.api.type.dto.ActivityGroupResponseDto;
import page.clab.api.type.dto.ActivityGroupStudyResponseDto;
import page.clab.api.type.dto.GroupMemberResponseDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.etc.ActivityGroupCategory;

@RestController
@RequestMapping("/activity-group/member")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupMember", description = "활동 그룹 멤버 관련 API")
@Slf4j
public class ActivityGroupMemberController {

    private final ActivityGroupMemberService activityGroupMemberService;

    @Operation(summary = "[U] 활동 전체 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @Secured({"ROLE_ANONYMOUS", "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getActivityGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupResponseDto> activityGroups = activityGroupMemberService.getActivityGroups(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroups);
        return responseModel;
    }

    @Operation(summary = "카테고리별 활동 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @Secured({"ROLE_ANONYMOUS", "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/list")
    public ResponseModel getActivityGroupsByCategory(
            @RequestParam ActivityGroupCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupResponseDto> activityGroups = activityGroupMemberService.getActivityGroupsByCategory(category, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroups);
        return responseModel;
    }

    @Operation(summary = "스터디 활동 상세 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @Secured({"ROLE_ANONYMOUS", "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/study/{activityGroupId}")
    public ResponseModel getActivityGroupStudy(
            @PathVariable Long activityGroupId
    ) {
        ActivityGroupStudyResponseDto activityGroup = activityGroupMemberService.getActivityGroupStudy(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroup);
        return responseModel;
    }

    @Operation(summary = "프로젝트 활동 상세 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @Secured({"ROLE_ANONYMOUS", "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/project/{activityGroupId}")
    public ResponseModel getActivityGroupProject(
            @PathVariable Long activityGroupId
    ) {
        ActivityGroupProjectResponseDto activityGroup = activityGroupMemberService.getActivityGroupProject(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroup);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 일정 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/schedule")
    public ResponseModel getGroupScheduleList(
            @RequestParam Long activityGroupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<GroupScheduleDto> groupSchedules = activityGroupMemberService.getGroupSchedules(activityGroupId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(groupSchedules);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 멤버 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/members")
    public ResponseModel getActivityGroupMemberList(
            @RequestParam Long activityGroupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<GroupMemberResponseDto> activityGroupMembers = activityGroupMemberService.getActivityGroupMembers(activityGroupId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMembers);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 신청", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/apply")
    public ResponseModel applyActivityGroup(
            @RequestParam Long activityGroupId
    ) throws MessagingException {
        Long id = activityGroupMemberService.applyActivityGroup(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}