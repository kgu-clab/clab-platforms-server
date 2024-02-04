package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.MemberStatus;
import page.clab.api.domain.member.dto.request.MemberRequestDto;
import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.common.verificationCode.dto.request.VerificationCodeRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버 관련 API")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "[A] 신규 멤버 생성", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createMember(
            @Valid @RequestBody MemberRequestDto memberRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        String id = memberService.createMember(memberRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 모든 멤버 정보 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getMembers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MemberResponseDto> members = memberService.getMembers(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(members);
        return responseModel;
    }

    @Operation(summary = "이달의 생일자 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/birthday")
    public ResponseModel getBirthdaysThisMonth(
            @RequestParam String month,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MemberResponseDto> birthdayMembers = memberService.getBirthdaysThisMonth(month, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(birthdayMembers);
        return responseModel;
    }

    @Operation(summary = "[A] 멤버 검색", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "멤버 ID, 이름, 상태를 기준으로 검색")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/search")
    public ResponseModel searchMember(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) MemberStatus memberStatus,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MemberResponseDto> members = memberService.searchMember(memberId, name, memberStatus, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(members);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 정보 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{memberId}")
    public ResponseModel updateMemberInfoByMember(
            @PathVariable(name = "memberId") String memberId,
            @Valid @RequestBody MemberRequestDto memberRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        String id = memberService.updateMemberInfo(memberId, memberRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 계정 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/status/{memberId}")
    public ResponseModel updateMemberStatusByAdmin(
            @PathVariable(name = "memberId") String memberId,
            @RequestParam MemberStatus memberStatus
    ) {
        String id = memberService.updateMemberStatusByAdmin(memberId, memberStatus);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "멤버 비밀번호 재발급 요청", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/password-reset-requests")
    public ResponseModel requestResetMemberPassword(
            @Valid @RequestBody MemberResetPasswordRequestDto memberResetPasswordRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        memberService.requestResetMemberPassword(memberResetPasswordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "멤버 비밀번호 재발급 인증", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/password-reset-verifications")
    public ResponseModel verifyResetMemberPassword(
            @Valid @RequestBody VerificationCodeRequestDto verificationCodeRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        memberService.verifyResetMemberPassword(verificationCodeRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 모든 멤버의 클라우드 사용량 조회", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "usage 단위: byte")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/cloud")
    public ResponseModel getAllCloudUsages(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<CloudUsageInfo> cloudUsageInfos = memberService.getAllCloudUsages(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(cloudUsageInfos);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버의 클라우드 사용량 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "usage 단위: byte")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/cloud/{memberId}")
    public ResponseModel getCloudUsageByMemberId(
            @PathVariable(name = "memberId") String memberId
    ) {
        CloudUsageInfo usage = memberService.getCloudUsageByMemberId(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(usage);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 업로드 파일 리스트 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/files/{memberId}")
    public ResponseModel getMemberUploadedFiles(
            @PathVariable(name = "memberId") String memberId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<FileInfo> files = memberService.getFilesInMemberDirectory(memberId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(files);
        return responseModel;
    }

    @Operation(summary = "[U] 내 프로필 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my-profile")
    public ResponseModel getMemberProfile(){
        MemberResponseDto memberResponseDto = memberService.getMemberProfile();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(memberResponseDto);
        return responseModel;
    }

}
