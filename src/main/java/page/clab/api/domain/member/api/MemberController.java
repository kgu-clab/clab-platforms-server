package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.dto.request.MemberRequestDto;
import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.domain.member.dto.request.MemberUpdateRequestDto;
import page.clab.api.domain.member.dto.response.MemberBirthdayResponseDto;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.domain.member.dto.response.MyProfileResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.common.verificationCode.dto.request.VerificationRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "[S] 신규 멤버 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createMember(
            @Valid @RequestBody MemberRequestDto requestDto
    ) {
        String id = memberService.createMember(requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[S] 모집 단위별 합격자 멤버 통합 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("/{recruitmentId}")
    public ResponseModel createMembersByRecruitmentId(
            @PathVariable(name = "recruitmentId") Long recruitmentId
    ) {
        List<String> ids = memberService.createMembersByRecruitmentId(recruitmentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(ids);
        return responseModel;
    }

    @Operation(summary = "[S] 모집 단위별 합격자 멤버 개별 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("/{recruitmentId}/{memberId}")
    public ResponseModel createMemberByRecruitmentId(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = memberService.createMemberByRecruitmentId(recruitmentId, memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 멤버 정보 조회(멤버 ID, 이름 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getMembersByConditions(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MemberResponseDto> members = memberService.getMembersByConditions(id, name, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(members);
        return responseModel;
    }

    @Operation(summary = "[U] 내 프로필 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my-profile")
    public ResponseModel getMyProfile() {
        MyProfileResponseDto myProfile = memberService.getMyProfile();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(myProfile);
        return responseModel;
    }

    @Operation(summary = "이달의 생일자 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/birthday")
    public ResponseModel getBirthdaysThisMonth(
            @RequestParam(name = "month") int month,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MemberBirthdayResponseDto> birthdayMembers = memberService.getBirthdaysThisMonth(month, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(birthdayMembers);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 정보 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{memberId}")
    public ResponseModel updateMemberInfoByMember(
            @PathVariable(name = "memberId") String memberId,
            @Valid @RequestBody MemberUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        String id = memberService.updateMemberInfo(memberId, requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "멤버 비밀번호 재발급 요청", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/password-reset-requests")
    public ResponseModel requestResetMemberPassword(
            @Valid @RequestBody MemberResetPasswordRequestDto requestDto
    ) {
        memberService.requestResetMemberPassword(requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "멤버 비밀번호 재발급 인증", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/password-reset-verifications")
    public ResponseModel verifyResetMemberPassword(
            @Valid @RequestBody VerificationRequestDto requestDto
    ) {
        memberService.verifyResetMemberPassword(requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
