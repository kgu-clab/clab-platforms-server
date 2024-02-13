package page.clab.api.global.common.file.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "UploadedFile", description = "파일 업로드 관련 API")
@Slf4j
public class FileController {

    private final FileService fileService;

    @Operation(summary = "[U] 게시글 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/boards/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel boardUpload(
            @PathVariable(name = "boardId") String boardId,
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        List<String> url = fileService.saveFiles(multipartFiles, "boards" + File.separator + boardId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 사진 업로드", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/news/{newsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel newsUpload(
            @PathVariable(name = "newsId") String newsId,
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        List<String> url = fileService.saveFiles(multipartFiles, "news" + File.separator + newsId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 사진 업로드", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/books/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel bookUpload(
            @PathVariable(name = "bookId") String bookId,
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        List<String> url = fileService.saveFiles(multipartFiles, "books" + File.separator + bookId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 프로필 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/profiles/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel profileUpload(
            @PathVariable(name = "memberId") String memberId,
            @RequestParam(name = "multipartFile") MultipartFile multipartFile,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        String url = fileService.saveFile(multipartFile, "profiles" + File.separator + memberId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 함께하는 활동 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/activity-photos/{activityPhotoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel activityUpload(
            @PathVariable(name = "activityPhotoId") String activityPhotoId,
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        List<String> url = fileService.saveFiles(multipartFiles, "activity-photos" + File.separator + activityPhotoId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 클라우드 파일 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/members/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel memberCloudUpload(
            @PathVariable(name = "memberId") String memberId,
            @RequestParam(name = "multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        List<String> url = fileService.saveFiles(multipartFiles, "members" + File.separator + memberId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 양식 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/forms", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel formUpload(
            @RequestParam("multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam("storagePeriod") long storagePeriod
    ) throws IOException, PermissionDeniedException {
        List<String> url = fileService.saveFiles(multipartFiles, "forms", storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 그룹 과제 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/assignment/{activityGroupId}/{activityGroupBoardId}/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel assignmentUpload(
            @PathVariable(name = "activityGroupId") Long activityGroupId,
            @PathVariable(name = "activityGroupBoardId") Long activityGroupBoardId,
            @PathVariable(name = "memberId") String memberId,
            @RequestParam(name = "multipartFile") MultipartFile multipartFile,
            @RequestParam(name = "storagePeriod") long storagePeriod
    ) throws PermissionDeniedException, IOException, NotFoundException {
        String url = fileService.saveFile(multipartFile,
                "assignment" + File.separator + activityGroupId + File.separator+ activityGroupBoardId + File.separator + memberId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 파일 삭제", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능<br>" + "/resources/files/~를 입력. 즉 생성시 전달받은 url을 입력.")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("")
    public ResponseModel deleteFile(@RequestBody DeleteFileRequestDto deleteFileRequestDto)
            throws PermissionDeniedException {
        String deletedFileUrl = fileService.deleteFile(deleteFileRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(deletedFileUrl);
        return responseModel;
    }

}
