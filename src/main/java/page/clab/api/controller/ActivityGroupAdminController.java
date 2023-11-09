package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.ActivityGroupDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.entity.Member;

import java.util.List;

@RestController
@RequestMapping("/activity-group/admin")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupAdmin")
@Slf4j
public class ActivityGroupAdminController {

    private final ActivityGroupAdminService activityGroupAdminService;

    private final MemberService memberService;

    @Operation(summary = "승인 대기 활동 조회", description = "승인 대기 활동 조회")
    @GetMapping("")
    public ResponseModel getWaitingActivityGroup (
    ) throws PermissionDeniedException {
        ResponseModel responseModel = ResponseModel.builder().build();
        memberService.checkMemberAdminRole();
        responseModel.addData(activityGroupAdminService.getWaitingActivityGroup());
        return responseModel;
    }


    @Operation(summary = "활동 승인", description = "활동 승인<br>")
    @PostMapping("/{id}/approve")
    public ResponseModel approveActivityGroup(
            @PathVariable Long id
    ) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        activityGroupAdminService.approveActivityGroup(id);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "활동 완료 상태 변경", description = "활동 완료 상태 변경")
    @PostMapping("/{id}/complete")
    public ResponseModel completeActivityGorup(
            @PathVariable Long id
    ) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        activityGroupAdminService.completeActivityGroup(id);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "활동 생성", description = "활동 생성<br>" +
            "String category; <br>" +
            "String title; <br>" +
            "String content; <br>" +
            "LocalDateTime createdAt; <br>" +
            "String writer; <br>")
    @PostMapping("")
    public ResponseModel createActivityGroup(
            @RequestBody ActivityGroupDto activityGroupDto
    ) {
        Member member = memberService.getCurrentMember();
        activityGroupAdminService.createActivityGroup(member, activityGroupDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "활동 수정")
    @PatchMapping("/{id}/update")
    public ResponseModel updateActivityGroup(
            @PathVariable Long id,
            @RequestBody ActivityGroupDto activityGroupDto
    ) throws PermissionDeniedException {
        memberService.checkMemberGroupLeaderRole();
        activityGroupAdminService.updateActivityGroup(id, activityGroupDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "활동 삭제")
    @DeleteMapping("/{id}/delete")
    public ResponseModel deleteActivityGroup(
            @PathVariable Long id
    ) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        activityGroupAdminService.deleteActivityGroup(id);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "프로젝트 진행도 수정")
    @PatchMapping("/{id}/progress")
    public ResponseModel updateProjectProgress(
            @PathVariable Long id,
            @RequestParam int progress
    ) throws PermissionDeniedException{
        memberService.checkMemberGroupLeaderRole();
        activityGroupAdminService.updateProjectProgress(id, progress);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "커리큘럼 및 일정 생성")
    @PatchMapping("/{id}/schedule")
    public ResponseModel addSchedule(
            @PathVariable Long id,
            @RequestBody List<GroupScheduleDto> groupScheduleDto
    ) throws PermissionDeniedException {
        memberService.checkMemberGroupLeaderRole();
        activityGroupAdminService.addSchedule(id, groupScheduleDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "멤버 인증 코드 생성")
    @PatchMapping("/{id}/member-auth")
    public ResponseModel createMemberAuthCode(
            @PathVariable Long id,
            @RequestParam String code
    ) throws PermissionDeniedException {
        memberService.checkMemberGroupLeaderRole();
        activityGroupAdminService.createMemberAuthCode(id, code);
        return ResponseModel.builder().build();
    }


}
