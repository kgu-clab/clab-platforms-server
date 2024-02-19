package page.clab.api.domain.activityGroup.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activityGroup.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupMemberApplierResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupStatusResponseDto;
import page.clab.api.domain.activityGroup.dto.response.GroupMemberResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/activity-group/member")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupMember", description = "활동 그룹 멤버")
@Slf4j
public class ActivityGroupMemberController {

    private final ActivityGroupMemberService activityGroupMemberService;

    @Operation(summary = "활동 전체 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getActivityGroups(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupResponseDto> activityGroups = activityGroupMemberService.getActivityGroups(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroups);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 상태별 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/status")
    public ResponseModel getActivityGroupsByStatus (
            @RequestParam(name = "activityGroupStatus") ActivityGroupStatus activityGroupStatus,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupStatusResponseDto> activityGroupList = activityGroupMemberService.getActivityGroupsByStatus(activityGroupStatus, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupList);
        return responseModel;
    }

    @Operation(summary = "카테고리별 활동 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/list")
    public ResponseModel getActivityGroupsByCategory(
            @RequestParam(name = "category") ActivityGroupCategory category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupResponseDto> activityGroups = activityGroupMemberService.getActivityGroupsByCategory(category, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroups);
        return responseModel;
    }

    @Operation(summary = "활동 상세 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/{activityGroupId}")
    public ResponseModel getActivityGroup(
            @PathVariable(name = "activityGroupId") Long activityGroupId
    ) {
        Object activityGroup = activityGroupMemberService.getActivityGroup(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroup);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 일정 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/schedule")
    public ResponseModel getGroupScheduleList(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<GroupScheduleDto> groupSchedules = activityGroupMemberService.getGroupSchedules(activityGroupId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(groupSchedules);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 멤버 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "활동에 참여(수락)된 멤버만 조회 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/members")
    public ResponseModel getActivityGroupMemberList(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
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
            @RequestParam Long activityGroupId,
            @Valid @RequestBody ApplyFormRequestDto formRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, MessagingException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = activityGroupMemberService.applyActivityGroup(activityGroupId, formRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 지원 폼에 들어갈 정보 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "지원폼을 작성할 때 로그인한 멤버에 대한 정보입니다.<br>" +
            "이를 지원 폼에 고정으로 넣어서 변경할 수 없도록 해주세요.")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/apply-form/applier-information")
    public ResponseModel getApplierInformation(){
        ActivityGroupMemberApplierResponseDto activityGroupMemberApplierResponseDto
                = activityGroupMemberService.getApplierInformation();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMemberApplierResponseDto);
        return responseModel;
    }

}