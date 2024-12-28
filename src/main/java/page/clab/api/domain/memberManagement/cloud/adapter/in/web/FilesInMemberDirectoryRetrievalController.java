package page.clab.api.domain.memberManagement.cloud.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.cloud.application.port.in.RetrieveFilesInMemberDirectoryUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.dto.response.FileInfo;

@RestController
@RequestMapping("/api/v1/clouds")
@RequiredArgsConstructor
@Tag(name = "Member Management - Cloud", description = "클라우드")
public class FilesInMemberDirectoryRetrievalController {

    private final RetrieveFilesInMemberDirectoryUseCase retrieveFilesInMemberDirectoryUseCase;

    @Operation(summary = "[U] 멤버 업로드 파일 리스트 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
        "본인 정보만 조회 가능")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/files/{memberId}")
    public ApiResponse<PagedResponseDto<FileInfo>> retrieveFilesInMemberDirectory(
        @PathVariable(name = "memberId") String memberId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<FileInfo> files = retrieveFilesInMemberDirectoryUseCase.retrieveFilesInMemberDirectory(
            memberId, pageable);
        return ApiResponse.success(files);
    }
}
