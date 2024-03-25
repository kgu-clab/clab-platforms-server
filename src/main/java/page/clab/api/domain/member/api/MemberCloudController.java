package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.MemberCloudService;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/member-clouds")
@RequiredArgsConstructor
@Tag(name = "Member Cloud", description = "멤버 클라우드")
@Slf4j
public class MemberCloudController {

    private final MemberCloudService memberCloudService;

    @Operation(summary = "[S] 모든 멤버의 클라우드 사용량 조회", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getAllCloudUsages(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<CloudUsageInfo> cloudUsageInfos = memberCloudService.getAllCloudUsages(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(cloudUsageInfos);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버의 클라우드 사용량 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{memberId}")
    public ResponseModel getCloudUsageByMemberId(
            @PathVariable(name = "memberId") String memberId
    ) throws PermissionDeniedException {
        CloudUsageInfo usage = memberCloudService.getCloudUsageByMemberId(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(usage);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 업로드 파일 리스트 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 정보만 조회 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/files/{memberId}")
    public ResponseModel getMemberUploadedFiles(
            @PathVariable(name = "memberId") String memberId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<FileInfo> files = memberCloudService.getFilesInMemberDirectory(memberId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(files);
        return responseModel;
    }

}
