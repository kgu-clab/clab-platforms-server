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
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "Member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "신규 유저 생성", description = "신규 유저 생성<br>" +
            "String id;<br>"+
            "String password;<br>" +
            "String name;<br>" +
            "String contact;<br>" +
            "String email;<br>" +
            "String department;<br>" +
            "Long grade;<br>" +
            "LocalDate birth; (ex: 2023-01-01)<br>" +
            "String address;<br>" +
            "Boolean isInSchool<br>")
    @PostMapping("")
    public ResponseModel createMember(
            @RequestBody MemberRequestDto memberRequestDto
    ) throws PermissionDeniedException {
        memberService.createMember(memberRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "모든 유저 정보 조회", description = "모든 유저 정보 조회")
    @GetMapping("")
    public ResponseModel getMembers() throws PermissionDeniedException {
        List<MemberResponseDto> users = memberService.getMembers();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(users);
        return responseModel;
    }

    @Operation(summary = "유저 검색", description = "유저의 ID 또는 이름을 기반으로 검색")
    @GetMapping("/search")
    public ResponseModel searchMember(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String name
    ) throws PermissionDeniedException {
        MemberResponseDto user = memberService.searchMember(memberId, name);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(user);
        return responseModel;
    }

    @Operation(summary = "유저 정보 수정", description = "본인 정보 수정<br>" +
            "String password;<br>"+
            "String name;<br>" +
            "String contact;<br>" +
            "String email;<br>" +
            "String department;<br>" +
            "Long grade;<br>" +
            "LocalDate birth; (ex: 2023-01-01)<br>" +
            "String address;<br>" +
            "Boolean isInSchool<br>" +
            "String imageUrl;")
    @PatchMapping("")
    public ResponseModel updateMemberInfoByMember(
            @RequestBody MemberUpdateRequestDto memberUpdateRequestDto
    ) {
        memberService.updateMemberInfoByMember(memberUpdateRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "유저 삭제(관리자 전용)", description = "관리자에 의한 유저 삭제(모든 계정 삭제 가능)")
    @DeleteMapping("/{memberId}")
    public ResponseModel deleteMemberByAdmin(
            @PathVariable("memberId") String memberId
    ) throws PermissionDeniedException {
        memberService.deleteMemberByAdmin(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "유저 삭제(일반 유저 전용)", description = "본인 계정 삭제")
    @DeleteMapping("")
    public ResponseModel deleteMemberByMember() {
        memberService.deleteMemberByMember();
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "모든 유저의 클라우드 사용량 조회", description = "모든 유저의 클라우드 사용량 조회<br>" +
            "usage 단위: byte")
    @GetMapping("/cloud")
    public ResponseModel getAllCloudUsages() {
        List<CloudUsageInfo> cloudUsageInfos = memberService.getAllCloudUsages();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(cloudUsageInfos);
        return responseModel;
    }

    @Operation(summary = "유저의 클라우드 사용량 조회", description = "유저의 클라우드 사용량 조회<br>" +
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

    @Operation(summary = "유저 업로드 파일 리스트 조회", description = "유저 업로드 파일 리스트 조회")
    @GetMapping("/{memberId}/files")
    public ResponseModel getMemberUploadedFiles(
            @PathVariable String memberId
    ) {
        List<FileInfo> files = memberService.getFilesInMemberDirectory(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(files);
        return responseModel;
    }

}
