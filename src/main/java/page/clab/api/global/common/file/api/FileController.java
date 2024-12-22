package page.clab.api.global.common.file.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
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

    @Operation(summary = "[U] 회비 증빙 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/membership-fee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> assignmentUpload(
        @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
        @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws PermissionDeniedException, IOException, NotFoundException {
        String path = fileService.buildPath("membership-fees");
        List<UploadedFileResponseDto> responseDtos = fileService.saveFiles(multipartFiles, path, storagePeriod);
        return ApiResponse.success(responseDtos);
    }

    @Operation(summary = "[U] 활동그룹 공지사항 파일 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/notices/{activityGroupId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> noticeUpload(
        @PathVariable(name = "activityGroupId") Long activityGroupId,
        @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
        @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws PermissionDeniedException, IOException {
        String path = fileService.buildPath("notices", activityGroupId);
        List<UploadedFileResponseDto> responseDtos = fileService.saveFiles(multipartFiles, path, storagePeriod);
        return ApiResponse.success(responseDtos);
    }

    @Operation(summary = "[U] 활동그룹 주차별 활동 파일 업로드", description = "ROLE_USER 이상의 권한이 필요함")
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

    @Operation(summary = "[U] 활동 그룹 과제 양식 업로드", description = "ROLE_USER 이상의 권한이 필요함 <br> 그룹장이 ASSIGNMENT 게시판에 올릴 파일을 업로드할 때 사용하는 API 입니다.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/assignments/{activityGroupId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> assignmentUpload(
        @PathVariable(name = "activityGroupId") Long activityGroupId,
        @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
        @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws PermissionDeniedException, IOException, NotFoundException {
        String path = fileService.buildPath("assignments", activityGroupId);
        List<UploadedFileResponseDto> responseDtos = fileService.saveFiles(multipartFiles, path, storagePeriod);
        return ApiResponse.success(responseDtos);
    }

    @Operation(summary = "[U] 활동 그룹 제출 파일 업로드", description =
        "ROLE_USER 이상의 권한이 필요함 <br> 그룹원이 SUBMIT 게시판에 올릴 파일을 업로드할 때 사용하는 API 입니다." +
            "<br> activityGroupBoardId는 ASSIGNMENT 게시판의 id를 입력하도록 합니다.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/submits/{activityGroupId}/{parentBoardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UploadedFileResponseDto>> submitUpload(
        @PathVariable(name = "activityGroupId") Long activityGroupId,
        @PathVariable(name = "parentBoardId") Long parentBoardId,
        @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
        @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws PermissionDeniedException, IOException, NotFoundException {
        String path = fileService.buildPath("submits", activityGroupId, parentBoardId);
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
