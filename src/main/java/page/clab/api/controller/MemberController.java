package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.type.dto.MemberUpdateRequestDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.etc.MemberStatus;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "Member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "신규 멤버 생성", description = "신규 멤버 생성<br>" +
            "StudentStatus: CURRENT / ON_LEAVE / GRADUATED<br>" +
            "MemberStatus: ACTIVE / INACTIVE")
    @PostMapping("")
    public ResponseModel createMember(
            @RequestBody MemberRequestDto memberRequestDto
    ) throws PermissionDeniedException {
        memberService.createMember(memberRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "모든 멤버 정보 조회", description = "모든 멤버 정보 조회")
    @GetMapping("")
    public ResponseModel getMembers() throws PermissionDeniedException {
        List<MemberResponseDto> members = memberService.getMembers();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(members);
        return responseModel;
    }

    @Operation(summary = "멤버 검색", description = "멤버의 ID 또는 이름을 기반으로 검색")
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

    @Operation(summary = "멤버 정보 수정", description = "본인 정보 수정<br>" +
            "StudentStatus: CURRENT / ON_LEAVE / GRADUATED")
    @PatchMapping("")
    public ResponseModel updateMemberInfoByMember(
            @RequestBody MemberUpdateRequestDto memberUpdateRequestDto
    ) {
        memberService.updateMemberInfoByMember(memberUpdateRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "계정 상태 변경(관리자 전용)", description = "관리자에 의한 계정 상태 변경")
    @PatchMapping("/status/{memberId}")
    public ResponseModel updateMemberStatusByAdmin(
            @PathVariable("memberId") String memberId,
            @RequestParam MemberStatus memberStatus
    ) throws PermissionDeniedException {
        memberService.updateMemberStatusByAdmin(memberId, memberStatus);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "모든 멤버의 클라우드 사용량 조회", description = "모든 멤버의 클라우드 사용량 조회<br>" +
            "usage 단위: byte")
    @GetMapping("/cloud")
    public ResponseModel getAllCloudUsages() {
        List<CloudUsageInfo> cloudUsageInfos = memberService.getAllCloudUsages();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(cloudUsageInfos);
        return responseModel;
    }

    @Operation(summary = "멤버의 클라우드 사용량 조회", description = "멤버의 클라우드 사용량 조회<br>" +
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

    @Operation(summary = "멤버 업로드 파일 리스트 조회", description = "멤버 업로드 파일 리스트 조회")
    @GetMapping("/files/{memberId}")
    public ResponseModel getMemberUploadedFiles(
            @PathVariable String memberId
    ) {
        List<FileInfo> files = memberService.getFilesInMemberDirectory(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(files);
        return responseModel;
    }

}
