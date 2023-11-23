package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.ActivityGroupMemberService;
import page.clab.api.type.dto.ActivityGroupDetailResponseDto;
import page.clab.api.type.dto.ActivityGroupRequestDto;
import page.clab.api.type.dto.ActivityGroupResponseDto;
import page.clab.api.type.dto.GroupMemberDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.etc.ActivityGroupCategory;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/activity-group/member")
@RequiredArgsConstructor
@Tag(name = "ActivityGroup", description = "활동 그룹 API")
@Slf4j
public class ActivityGroupMemberController {

    private final ActivityGroupMemberService activityGroupMemberService;

    @Operation(summary = "활동 목록 조회(카테고리별)", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/{category}")
    public ResponseModel getActivityGroups(
            @PathVariable ActivityGroupCategory category
    ) {
        List<ActivityGroupResponseDto> activityGroups = activityGroupMemberService.getActivityGroups(category);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroups);
        return responseModel;
    }

    @Operation(summary = "활동 상세 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("details/{activityGroupId}")
    public ResponseModel getActivityGroup(
            @PathVariable Long activityGroupId
    ) {
        ActivityGroupDetailResponseDto activityGroup = activityGroupMemberService.getActivityGroup(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroup);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 일정 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/schedule")
    public ResponseModel getGroupScheduleList(
            @RequestParam Long activityGroupId
    ) {
        List<GroupScheduleDto> groupSchedules = activityGroupMemberService.getGroupSchedules(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(groupSchedules);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 멤버 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/members")
    public ResponseModel getActivityGroupMemberList(
            @RequestParam Long activityGroupId
    ) {
        List<GroupMemberDto> activityGroupMembers = activityGroupMemberService.getActivityGroupMembers(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMembers);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 신청", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/apply")
    public ResponseModel applyActivityGroup(
            @RequestParam Long activityGroupId
    ) throws MessagingException {
        activityGroupMemberService.applyActivityGroup(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
