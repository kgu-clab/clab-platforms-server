package page.clab.api.global.common.file.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "UploadedFile", description = "파일 업로드")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "[U] 게시글 파일 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/boards", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> boardUpload(
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        String path = fileService.buildPath("boards");
        List<UploadedFileResponseDto> responseDtos = fileService.saveFiles(multipartFiles, path, storagePeriod);
        return ApiResponse.success(responseDtos);
    }

    @Operation(summary = "[U] 멤버 프로필 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/profiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UploadedFileResponseDto> profileUpload(
            @RequestParam(name = "multipartFile") MultipartFile multipartFile,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        String path = fileService.buildPath("profiles");
        UploadedFileResponseDto responseDto = fileService.saveFile(multipartFile, path, storagePeriod);
        return ApiResponse.success(responseDto);
    }

    @Operation(summary = "[A] 함께하는 활동 사진 업로드", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/activity-photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> activityUpload(
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        String path = fileService.buildPath("activity-photos");
        List<UploadedFileResponseDto> responseDtos = fileService.saveFiles(multipartFiles, path, storagePeriod);
        return ApiResponse.success(responseDtos);
    }

    @Operation(summary = "[U] 멤버 클라우드 파일 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> memberCloudUpload(
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        String path = fileService.buildPath("members");
        List<UploadedFileResponseDto> responseDtos = fileService.saveFiles(multipartFiles, path, storagePeriod);
        return ApiResponse.success(responseDtos);
    }

    @Operation(summary = "[U] 주차별 활동 파일 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/weekly-activities/{activityGroupId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> weeklyActivityUpload(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        String path = fileService.buildPath("weekly-activities", activityGroupId);
        List<UploadedFileResponseDto> responseDtos = fileService.saveFiles(multipartFiles, path, storagePeriod);
        return ApiResponse.success(responseDtos);
    }

    @Operation(summary = "[U] 활동 그룹 과제 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/assignment/{activityGroupId}/{activityGroupBoardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> assignmentUpload(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @PathVariable(name = "activityGroupBoardId") Long activityGroupBoardId,
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws PermissionDeniedException, IOException, NotFoundException {
        String path = fileService.buildPath("assignments", activityGroupId, activityGroupBoardId);
        List<UploadedFileResponseDto> responseDtos = fileService.saveFiles(multipartFiles, path, storagePeriod);
        return ApiResponse.success(responseDtos);
    }

    @Operation(summary = "[U] 파일 삭제", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능<br>" + "/resources/files/~를 입력. 즉 생성시 전달받은 url을 입력.")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public ApiResponse<String> deleteFile(@RequestBody DeleteFileRequestDto deleteFileRequestDto)
            throws PermissionDeniedException {
        String deletedFileUrl = fileService.deleteFile(deleteFileRequestDto);
        return ApiResponse.success(deletedFileUrl);
    }
}
