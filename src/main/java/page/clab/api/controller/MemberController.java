package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.CloudUsageInfo;
import page.clab.api.type.dto.FileInfo;
import page.clab.api.type.dto.MemberRequestDto;
import page.clab.api.type.dto.MemberResponseDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.etc.MemberStatus;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버 관련 API")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "[A] 신규 멤버 생성", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createMember(
            @Valid @RequestBody MemberRequestDto memberRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        memberService.createMember(memberRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 모든 멤버 정보 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getMembers() throws PermissionDeniedException {
        List<MemberResponseDto> members = memberService.getMembers();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(members);
        return responseModel;
    }

    @Operation(summary = "[A] 멤버 검색", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "멤버 ID, 이름, 상태를 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel searchMember(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) MemberStatus memberStatus
    ) throws PermissionDeniedException {
        List<MemberResponseDto> members = memberService.searchMember(memberId, name, memberStatus);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(members);
        return responseModel;
    }

  @Operation(summary = "[U] 멤버 정보 수정", description = "ROLE_USER 이상의 권한이 필요함")
  @PatchMapping("/{memberId}")
  public ResponseModel updateMemberInfoByMember(
      @PathVariable String memberId,
      @Valid @RequestBody MemberRequestDto memberRequestDto,
      BindingResult result)
      throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        memberService.updateMemberInfo(memberId, memberRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 계정 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PatchMapping("/status/{memberId}")
    public ResponseModel updateMemberStatusByAdmin(
            @PathVariable String memberId,
            @RequestParam MemberStatus memberStatus
    ) throws PermissionDeniedException {
        memberService.updateMemberStatusByAdmin(memberId, memberStatus);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 모든 멤버의 클라우드 사용량 조회", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "usage 단위: byte")
    @GetMapping("/cloud")
    public ResponseModel getAllCloudUsages() throws PermissionDeniedException {
        List<CloudUsageInfo> cloudUsageInfos = memberService.getAllCloudUsages();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(cloudUsageInfos);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버의 클라우드 사용량 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "usage 단위: byte")
    @GetMapping("/cloud/{memberId}")
    public ResponseModel getCloudUsageByMemberId(
            @PathVariable String memberId
    ) {
        CloudUsageInfo usage = memberService.getCloudUsageByMemberId(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(usage);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 업로드 파일 리스트 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/files/{memberId}")
    public ResponseModel getMemberUploadedFiles(
            @PathVariable String memberId
    ) {
        List<FileInfo> files = memberService.getFilesInMemberDirectory(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(files);
        return responseModel;
    }

//    @Operation(summary = "활동 그룹 팀장 권한 부여", description = "활동 그룹 팀장 권한 부여")
//    @PostMapping("/{memberId}/appointment")
//    public ResponseModel grantActivityGroupLeaderRole(
//            @PathVariable String memberId,
//            @RequestParam Long activityGroupId
//    ) throws PermissionDeniedException {
//        memberService.grantActivityGroupLeaderRole(memberId, activityGroupId);
//        return ResponseModel.builder().build();
//    }

}
