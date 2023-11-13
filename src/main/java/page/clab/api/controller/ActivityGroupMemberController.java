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
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/activity-group/member")
@RequiredArgsConstructor
@Tag(name = "ActivityGroup")
@Slf4j
public class ActivityGroupMemberController {

    private final ActivityGroupMemberService activityGroupMemberService;

    private final MemberService memberService;

    @Operation(summary = "프로젝트 리스팅")
    @GetMapping("/products")
    public ResponseModel getProjectList(
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMemberService.getProjectList());
        return responseModel;
    }

    @Operation(summary = "스터디 리스팅")
    @GetMapping("/studies")
    public ResponseModel getStudyList(
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMemberService.getStudyList());
        return responseModel;
    }

    @Operation(summary = "프로젝트 조회")
    @GetMapping("/{id}/project")
    public ResponseModel getProject(
            @PathVariable Long id
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMemberService.getProjectGroup(id));
        return responseModel;
    }

    @Operation(summary = "스터디 조회")
    @GetMapping("/{id}/study")
    public ResponseModel getStudy(
            @PathVariable Long id
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMemberService.getStudyGroup(id));
        return responseModel;
    }

    @Operation(summary = "활동 일정 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/schedule")
    public ResponseModel getGroupScheduleList(
            @RequestParam Long id
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMemberService.getGroupScheduleList(id));
        return responseModel;
    }

    @Operation(summary = "활동 멤버 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/members")
    public ResponseModel getActivityGroupMemberList(
            @RequestParam Long id
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupMemberService.getActivityGroupMemberList(id));
        return responseModel;
    }

    @Operation(summary = "활동 멤버 인증", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/auth")
    public ResponseModel authenticateActivityMember(
            @RequestParam Long id,
            @RequestParam String code
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        activityGroupMemberService.authenticateActivityMember(id, code);
        return responseModel;
    }

}
